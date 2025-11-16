-- ==============================
-- Function: handle_new_user
-- Automatically creates a new users record when an auth.users record was created
-- ==============================
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger
SET search_path = ''
AS $$
BEGIN
  INSERT INTO public.users (id, email, username)
  VALUES (
    NEW.id,
    NEW.email,
    NEW.raw_user_meta_data->>'display_name' -- FROM AUTH USERS display_name FIELD
  );
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Trigger: on_auth_user_created
-- ==============================
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'on_auth_user_created'
      AND c.relname = 'users'
      AND t.tgenabled <> 'D'
  ) THEN
    CREATE TRIGGER on_auth_user_created
      AFTER INSERT ON auth.users
      FOR EACH ROW
      EXECUTE PROCEDURE public.handle_new_user();
  END IF;
END;
$$;

-- ==============================
-- Function: handle_new_user_private_household
-- Automatically creates a private household for new users
-- ==============================
CREATE OR REPLACE FUNCTION public.handle_new_user_private_household()
RETURNS trigger
SET search_path = ''
AS $$
DECLARE
  new_household_id uuid;
BEGIN
  -- Create a new household
  INSERT INTO public.households (name)
  VALUES ('Private household')
  RETURNING id INTO new_household_id;

  -- Create a household member record with the user as an owner
  INSERT INTO public.household_members (household_id, user_id, role)
  VALUES (new_household_id, NEW.id, 'owner');

  -- Create default categories
  PERFORM public.create_default_categories(new_household_id);

  -- Create default payment methods
  PERFORM public.create_default_payment_methods(NEW.id, new_household_id);

  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Trigger: on_public_user_created_household
-- ==============================
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'on_public_user_created_household'
      AND c.relname = 'users'
      AND t.tgenabled <> 'D'
  ) THEN
    CREATE TRIGGER on_public_user_created_household
      AFTER INSERT ON public.users
      FOR EACH ROW
      EXECUTE PROCEDURE public.handle_new_user_private_household();
  END IF;
END;
$$;

-- ==============================
-- Function: create_default_categories
-- Creates default categories for a new household
-- ==============================
CREATE OR REPLACE FUNCTION public.create_default_categories(
  p_household_id uuid
)
RETURNS void
SET search_path = ''
AS $$
BEGIN
  INSERT INTO public.categories (household_id, name, icon, color) VALUES
    (p_household_id, 'Food & Dining', 'icon', '#FF6B6B'),
    (p_household_id, 'Groceries', 'icon', '#4ECDC4'),
    (p_household_id, 'Transportation', 'icon', '#45B7D1'),
    (p_household_id, 'Entertainment', 'icon', '#96CEB4'),
    (p_household_id, 'Shopping', 'icon', '#FFEAA7'),
    (p_household_id, 'Bills & Utilities', 'icon', '#FD79A8'),
    (p_household_id, 'Healthcare', 'icon', '#A29BFE'),
    (p_household_id, 'Education', 'icon', '#6C5CE7'),
    (p_household_id, 'Home & Garden', 'icon', '#FD6C6C'),
    (p_household_id, 'Personal Care', 'icon', '#FDCB6E'),
    (p_household_id, 'Subscriptions', 'icon', '#E17055'),
    (p_household_id, 'Travel', 'icon', '#00B894'),
    (p_household_id, 'Income', 'icon', '#00CEC9'),
    (p_household_id, 'Other', 'icon', '#636E72');
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Function: create_default_payment_methods
-- Creates default payment methods for a new user and links them to the household
-- ==============================
CREATE OR REPLACE FUNCTION public.create_default_payment_methods(
  p_user_id uuid,
  p_household_id uuid
)
RETURNS void
SET search_path = ''
AS $$
DECLARE
  pm_id uuid;
BEGIN
  -- Validate that user is member of household first
  IF NOT public.is_household_member(p_household_id, p_user_id) THEN
    RAISE EXCEPTION 'User is not a member of the household' USING ERRCODE = '42501';
  END IF;

  -- Create Cash payment method
  INSERT INTO public.payment_methods (owner_user_id, name, icon, color, type) 
  VALUES (p_user_id, 'Cash', 'cash', '#2ECC71', 'cash')
  RETURNING id INTO pm_id;
  
  INSERT INTO public.household_payment_methods (household_id, payment_method_id)
  VALUES (p_household_id, pm_id);

  -- Create Credit Card payment method
  INSERT INTO public.payment_methods (owner_user_id, name, icon, color, type) 
  VALUES (p_user_id, 'Credit Card', 'credit-card', '#3498DB', 'card')
  RETURNING id INTO pm_id;
  
  INSERT INTO public.household_payment_methods (household_id, payment_method_id)
  VALUES (p_household_id, pm_id);

  -- Create Bank Transfer payment method
  INSERT INTO public.payment_methods (owner_user_id, name, icon, color, type) 
  VALUES (p_user_id, 'Bank Transfer', 'bank', '#E67E22', 'bank_account')
  RETURNING id INTO pm_id;
  
  INSERT INTO public.household_payment_methods (household_id, payment_method_id)
  VALUES (p_household_id, pm_id);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Function: calculate_next_due_date
-- Calculates the next due date based on recurrence period
-- ==============================
CREATE OR REPLACE FUNCTION public.calculate_next_due_date(
  base_date date,
  period recurrence_period
)
RETURNS date
SET search_path = ''
AS $$
BEGIN
  CASE period
    WHEN 'weekly' THEN
      RETURN base_date + INTERVAL '1 week';
    WHEN 'monthly' THEN
      RETURN base_date + INTERVAL '1 month';
    WHEN 'quarterly' THEN
      RETURN base_date + INTERVAL '3 months';
    WHEN 'yearly' THEN
      RETURN base_date + INTERVAL '1 year';
    ELSE
      RETURN base_date + INTERVAL '1 month';
  END CASE;
END;
$$ LANGUAGE plpgsql;

-- ==============================
-- Function: update_recurring_payment_next_due
-- Updates next_due_date when a recurring payment is marked as paid
-- ==============================
CREATE OR REPLACE FUNCTION public.update_recurring_payment_next_due()
RETURNS trigger
SET search_path = ''
AS $$
BEGIN
  -- Only update if last_paid_date changed
  IF NEW.last_paid_date IS DISTINCT FROM OLD.last_paid_date AND NEW.last_paid_date IS NOT NULL THEN
    NEW.next_due_date := public.calculate_next_due_date(NEW.last_paid_date, NEW.recurrence_period);
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Function: update_recurring_payment_on_transaction
-- Updates recurring payment when a transaction is created with recurring_payment_id
-- ==============================
CREATE OR REPLACE FUNCTION public.update_recurring_payment_on_transaction()
RETURNS trigger
SET search_path = ''
AS $$
DECLARE
  recurring_payment_record RECORD;
BEGIN
  -- Only process if recurring_payment_id is not null
  IF NEW.recurring_payment_id IS NOT NULL THEN
    -- Get the recurring payment details
    SELECT * INTO recurring_payment_record
    FROM public.recurring_payments
    WHERE id = NEW.recurring_payment_id AND is_active = true;
    
    IF FOUND THEN
      -- Update the recurring payment's last_paid_date and calculate next_due_date
      UPDATE public.recurring_payments
      SET 
        last_paid_date = NEW.transaction_date,
        next_due_date = public.calculate_next_due_date(NEW.transaction_date, recurring_payment_record.recurrence_period)
      WHERE id = NEW.recurring_payment_id;
    END IF;
  END IF;
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Trigger: on_transaction_with_recurring_payment
-- ==============================
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'on_transaction_with_recurring_payment'
      AND c.relname = 'transactions'
      AND t.tgenabled <> 'D'
  ) THEN
    CREATE TRIGGER on_transaction_with_recurring_payment
      AFTER INSERT ON public.transactions
      FOR EACH ROW
      EXECUTE PROCEDURE public.update_recurring_payment_on_transaction();
  END IF;
END;
$$;

-- ==============================
-- Function: is_household_member
-- Security definer function to check household membership without RLS
-- ==============================
CREATE OR REPLACE FUNCTION public.is_household_member(
  p_household_id uuid,
  p_user_id uuid DEFAULT auth.uid()
)
RETURNS boolean
SECURITY DEFINER
SET search_path = public
AS $$
BEGIN
  RETURN EXISTS (
    SELECT 1 
    FROM household_members hm
    WHERE hm.household_id = p_household_id
      AND hm.user_id = p_user_id
  );
END;
$$ LANGUAGE plpgsql;

-- ==============================
-- Function: add_payment_method
-- Creates a payment_method for p_user_id and links it to p_household_id
-- ==============================
CREATE OR REPLACE FUNCTION public.add_payment_method(
  p_user_id uuid,
  p_household_id uuid,
  p_name text,
  p_icon text DEFAULT NULL,
  p_color text DEFAULT NULL,
  p_type payment_method_type DEFAULT 'card'
)
RETURNS uuid
SET search_path = ''
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
  v_pm_id uuid;
  v_auth_uid uuid := (SELECT auth.uid());
BEGIN
  -- Guard: callers can only act for themselves
  IF p_user_id IS DISTINCT FROM v_auth_uid THEN
    RAISE EXCEPTION 'Not allowed: user mismatch' USING ERRCODE = '28000';
  END IF;

  -- Guard: user must be a member of the household
  IF NOT public.is_household_member(p_household_id, p_user_id) THEN
    RAISE EXCEPTION 'User is not a member of the household' USING ERRCODE = '42501';
  END IF;

  -- Create payment method
  INSERT INTO public.payment_methods (owner_user_id, name, icon, color, type)
  VALUES (p_user_id, p_name, p_icon, p_color, p_type)
  RETURNING id INTO v_pm_id;

  -- Link to household
  INSERT INTO public.household_payment_methods (household_id, payment_method_id)
  VALUES (p_household_id, v_pm_id);

  RETURN v_pm_id;
END;
$$;

-- ==============================
-- Trigger: on_recurring_payment_updated
-- ==============================
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'on_recurring_payment_updated'
      AND c.relname = 'recurring_payments'
      AND t.tgenabled <> 'D'
  ) THEN
    CREATE TRIGGER on_recurring_payment_updated
      BEFORE UPDATE ON public.recurring_payments
      FOR EACH ROW
      EXECUTE PROCEDURE public.update_recurring_payment_next_due();
  END IF;
END;
$$;

-- ==============================
-- Function: delete_transaction
-- Deletes a transaction if the user is the creator or a household owner
-- ==============================
CREATE OR REPLACE FUNCTION public.delete_transaction(
  p_transaction_id uuid
)
RETURNS void
SECURITY DEFINER
SET search_path = public
AS $$
DECLARE
  v_user_id uuid;
  v_household_id uuid;
  v_is_owner boolean;
BEGIN
  -- Get current user
  v_user_id := auth.uid();
  
  IF v_user_id IS NULL THEN
    RAISE EXCEPTION 'Not authenticated' USING ERRCODE = '42501';
  END IF;
  
  -- Get transaction details
  SELECT household_id, user_id 
  INTO v_household_id, v_user_id
  FROM transactions
  WHERE id = p_transaction_id;
  
  IF NOT FOUND THEN
    RAISE EXCEPTION 'Transaction not found' USING ERRCODE = '42704';
  END IF;
  
  -- Check if user is transaction creator
  IF v_user_id = auth.uid() THEN
    DELETE FROM transactions WHERE id = p_transaction_id;
    RETURN;
  END IF;
  
  -- Check if user is household owner
  SELECT EXISTS (
    SELECT 1 
    FROM household_members hm
    WHERE hm.household_id = v_household_id
      AND hm.user_id = auth.uid()
      AND hm.role = 'owner'
  ) INTO v_is_owner;
  
  IF v_is_owner THEN
    DELETE FROM transactions WHERE id = p_transaction_id;
    RETURN;
  END IF;
  
  -- User is neither creator nor owner
  RAISE EXCEPTION 'Permission denied' USING ERRCODE = '42501';
END;
$$ LANGUAGE plpgsql;

-- Grant execute permission
GRANT EXECUTE ON FUNCTION public.delete_transaction(uuid) TO authenticated;

