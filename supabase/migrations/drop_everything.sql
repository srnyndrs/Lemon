BEGIN;

-- 1) Drop known triggers (if present)
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
DROP TRIGGER IF EXISTS on_public_user_created_household ON public.users;

-- 2) Drop views
DROP VIEW IF EXISTS
  public.user_households,
  public.household_payment_methods_view,
  public.household_categories_view,
  public.household_transactions_view,
  public.household_members_view,
  public.household_monthly_expenses_view,
  public.household_summary_view,
  public.household_details_view,
  public.users_view
CASCADE;

-- 3) Drop functions (explicit signatures where applicable)
DROP FUNCTION IF EXISTS public.handle_new_user() CASCADE;
DROP FUNCTION IF EXISTS public.create_household_for_user(uuid, text) CASCADE;
DROP FUNCTION IF EXISTS public.handle_new_user_private_household(text) CASCADE;
DROP FUNCTION IF EXISTS public.trigger_create_default_household_for_new_user() CASCADE;
DROP FUNCTION IF EXISTS public.create_default_categories(uuid) CASCADE;
DROP FUNCTION IF EXISTS public.create_default_payment_methods(uuid, uuid) CASCADE;
DROP FUNCTION IF EXISTS public.is_household_member(uuid, uuid) CASCADE;
DROP FUNCTION IF EXISTS public.add_payment_method(uuid, uuid, text, text, text, payment_method_type) CASCADE;
DROP FUNCTION IF EXISTS public.delete_transaction(uuid) CASCADE;
DROP FUNCTION IF EXISTS public.is_household_owner(uuid, uuid) CASCADE;
DROP FUNCTION IF EXISTS public.get_household_details(uuid) CASCADE;
DROP FUNCTION IF EXISTS public.update_household_name(uuid, text) CASCADE;
DROP FUNCTION IF EXISTS public.add_household_member(uuid, uuid, household_member_type) CASCADE;
DROP FUNCTION IF EXISTS public.remove_household_member(uuid, uuid) CASCADE;
DROP FUNCTION IF EXISTS public.update_household_member_role(uuid, uuid, household_member_type) CASCADE;
DROP FUNCTION IF EXISTS public.delete_household(uuid) CASCADE;
DROP FUNCTION IF EXISTS public.delete_category(uuid) CASCADE;
DROP FUNCTION IF EXISTS public.update_category(uuid, text, text, text) CASCADE;
DROP FUNCTION IF EXISTS public.update_payment_method(uuid, text, text, text, payment_method_type, boolean) CASCADE;
DROP FUNCTION IF EXISTS public.delete_payment_method(uuid) CASCADE;
-- Drop any remaining functions by pattern (optional)

-- 4) Drop tables (order not critical with CASCADE, but keep logical order)
DROP TABLE IF EXISTS public.transactions CASCADE;
DROP TABLE IF EXISTS public.household_payment_methods CASCADE;
DROP TABLE IF EXISTS public.categories CASCADE;
DROP TABLE IF EXISTS public.payment_methods CASCADE;
DROP TABLE IF EXISTS public.household_members CASCADE;
DROP TABLE IF EXISTS public.households CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;

-- 5) Drop types
DROP TYPE IF EXISTS public.household_member_type CASCADE;
DROP TYPE IF EXISTS public.payment_method_type CASCADE;
DROP TYPE IF EXISTS public.transaction_type CASCADE;

-- 6) Drop indexes that might exist outside table drops
DROP INDEX IF EXISTS idx_household_payment_methods_payment_method_id;
DROP INDEX IF EXISTS idx_transactions_payment_method_id;

-- 7) Drop any remaining policies (if any left)
-- If tables are dropped with CASCADE this is usually redundant, but keep for safety
DO $$
BEGIN
  PERFORM 1
  FROM pg_policy
  LIMIT 1;
  -- If pg_policy exists, attempt to drop by searching policies attached to known tables
  EXECUTE 'DROP POLICY IF EXISTS "Users can view all user records" ON public.users';
  EXECUTE 'DROP POLICY IF EXISTS "Users can insert their own user record" ON public.users';
  EXECUTE 'DROP POLICY IF EXISTS "Users can update their own user record" ON public.users';
  EXECUTE 'DROP POLICY IF EXISTS "Users can view households they belong to" ON public.households';
  EXECUTE 'DROP POLICY IF EXISTS "Authenticated users can create households" ON public.households';
  EXECUTE 'DROP POLICY IF EXISTS "Only household owners can update household" ON public.households';
  EXECUTE 'DROP POLICY IF EXISTS "Only household owners can delete household" ON public.households';
  EXECUTE 'DROP POLICY IF EXISTS "Users can view household members in shared households" ON public.household_members';
  EXECUTE 'DROP POLICY IF EXISTS "Users can add themselves or owners can add others" ON public.household_members';
  EXECUTE 'DROP POLICY IF EXISTS "Only owners can remove members" ON public.household_members';
  EXECUTE 'DROP POLICY IF EXISTS "Users can view payment methods available to them" ON public.payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Users can create their own payment methods" ON public.payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Only owner can update payment method" ON public.payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Only owner can delete payment method" ON public.payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Members can view household payment methods" ON public.household_payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Members can link their payment methods" ON public.household_payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Payment method owners or household owners can unlink" ON public.household_payment_methods';
  EXECUTE 'DROP POLICY IF EXISTS "Household members can view categories" ON public.categories';
  EXECUTE 'DROP POLICY IF EXISTS "Household members can create categories" ON public.categories';
  EXECUTE 'DROP POLICY IF EXISTS "Household members can update categories" ON public.categories';
  EXECUTE 'DROP POLICY IF EXISTS "Household owners can delete categories" ON public.categories';
  EXECUTE 'DROP POLICY IF EXISTS "Household members can view transactions" ON public.transactions';
  EXECUTE 'DROP POLICY IF EXISTS "Household members can create transactions" ON public.transactions';
  EXECUTE 'DROP POLICY IF EXISTS "Transaction creators or household owners can update" ON public.transactions';
  EXECUTE 'DROP POLICY IF EXISTS "Transaction creators or household owners can delete" ON public.transactions'; 
EXCEPTION WHEN others THEN
  -- ignore errors here; it's a best-effort cleanup
  RAISE NOTICE 'Policy cleanup: ignored error %', SQLERRM;
END$$;

COMMIT;

-- End of drop-all migration
