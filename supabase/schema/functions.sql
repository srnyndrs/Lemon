-- ==============================
-- Function: handle_new_user
-- Automatikusan létrehoz egy users rekordot, amikor egy új auth.users bejegyzés születik
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
    NEW.raw_user_meta_data->>'display_name' --COMES FROM SUPABASE AUTH USERS display_name FIELD
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
-- Automatikusan létrehoz egy privát householdot az új usernek
-- ==============================
CREATE OR REPLACE FUNCTION public.handle_new_user_private_household()
RETURNS trigger
SET search_path = ''
AS $$
DECLARE
  new_household_id uuid;
BEGIN
  -- létrehozunk egy új householdot
  INSERT INTO public.households (name)
  VALUES ('Private household')
  RETURNING id INTO new_household_id;

  -- hozzáadjuk a usert ownerként
  INSERT INTO public.household_members (household_id, user_id, role)
  VALUES (new_household_id, NEW.id, 'owner');

  -- létrehozunk alapértelmezett kategóriákat
  PERFORM public.create_default_categories(new_household_id);

  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- ==============================
-- Trigger: on_auth_user_created_household
-- ==============================
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_trigger t
    JOIN pg_class c ON t.tgrelid = c.oid
    WHERE t.tgname = 'on_auth_user_created_household'
      AND c.relname = 'users'
      AND t.tgenabled <> 'D'
  ) THEN
    CREATE TRIGGER on_auth_user_created_household
      AFTER INSERT ON auth.users
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
      RETURN base_date + INTERVAL '1 month'; -- default to monthly
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
