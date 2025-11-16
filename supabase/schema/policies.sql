-- ========================
-- USERS
-- ========================
DROP POLICY IF EXISTS "Users can view all user records" ON public.users;
CREATE POLICY "Users can view all user records"
  ON public.users FOR SELECT
  USING (true);

DROP POLICY IF EXISTS "Users can insert their own user record" ON public.users;
CREATE POLICY "Users can insert their own user record"
  ON public.users FOR INSERT 
  WITH CHECK (auth.uid() = id);

DROP POLICY IF EXISTS "Users can update their own user record" ON public.users;
CREATE POLICY "Users can update their own user record" 
  ON public.users FOR UPDATE
  USING (auth.uid() = id);

-- ========================
-- HOUSEHOLDS
-- ========================
DROP POLICY IF EXISTS "Users can view households they belong to" ON public.households;
CREATE POLICY "Users can view households they belong to"
  ON public.households FOR SELECT
  USING (
    public.is_household_member(id, auth.uid())
  );

DROP POLICY IF EXISTS "Authenticated users can create households" ON public.households;
CREATE POLICY "Authenticated users can create households"
  ON public.households FOR INSERT
  WITH CHECK (auth.uid() IS NOT NULL);

DROP POLICY IF EXISTS "Only household owners can update household" ON public.households;
CREATE POLICY "Only household owners can update household"
  ON public.households FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Only household owners can delete household" ON public.households;
CREATE POLICY "Only household owners can delete household"
  ON public.households FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

-- ========================
-- HOUSEHOLD MEMBERS
-- ========================
DROP POLICY IF EXISTS "Users can view household members in shared households" ON public.household_members;
CREATE POLICY "Users can view household members in shared households"
  ON public.household_members FOR SELECT
  USING (
    -- Users can see members of households where they have a direct membership
    household_id IN (
      SELECT hm2.household_id 
      FROM public.household_members hm2 
      WHERE hm2.user_id = auth.uid()
    )
  );

DROP POLICY IF EXISTS "Users can add themselves or owners can add others" ON public.household_members;
CREATE POLICY "Users can add themselves or owners can add others"
  ON public.household_members FOR INSERT
  WITH CHECK (
    -- Users can add themselves as owner (initial household creation)
    (user_id = auth.uid() AND role = 'owner')
    OR
    -- Existing owners can add new members
    (household_id IN (
      SELECT hm.household_id 
      FROM public.household_members hm 
      WHERE hm.user_id = auth.uid() 
        AND hm.role = 'owner'
    ))
  );

DROP POLICY IF EXISTS "Only owners can remove members" ON public.household_members;
CREATE POLICY "Only owners can remove members"
  ON public.household_members FOR DELETE
  USING (
    household_id IN (
      SELECT hm.household_id 
      FROM public.household_members hm 
      WHERE hm.user_id = auth.uid() 
        AND hm.role = 'owner'
    )
  );

-- ========================
-- PAYMENT METHODS
-- ========================
DROP POLICY IF EXISTS "Users can view payment methods available to them" ON public.payment_methods;
CREATE POLICY "Users can view payment methods available to them"
  ON public.payment_methods FOR SELECT
  USING (
    -- Own payment methods
    owner_user_id = auth.uid()
    OR
    -- Payment methods shared via households
    EXISTS (
      SELECT 1 FROM public.household_payment_methods hpm
      JOIN public.household_members hm ON hpm.household_id = hm.household_id
      WHERE hpm.payment_method_id = id
        AND hm.user_id = auth.uid()
    )
  );

DROP POLICY IF EXISTS "Users can create their own payment methods" ON public.payment_methods;
CREATE POLICY "Users can create their own payment methods"
  ON public.payment_methods FOR INSERT
  WITH CHECK (owner_user_id = auth.uid());

DROP POLICY IF EXISTS "Only owner can update payment method" ON public.payment_methods;
CREATE POLICY "Only owner can update payment method"
  ON public.payment_methods FOR UPDATE
  USING (owner_user_id = auth.uid());

DROP POLICY IF EXISTS "Only owner can delete payment method" ON public.payment_methods;
CREATE POLICY "Only owner can delete payment method"
  ON public.payment_methods FOR DELETE
  USING (owner_user_id = auth.uid());

-- ========================
-- HOUSEHOLD PAYMENT METHODS
-- ========================
DROP POLICY IF EXISTS "Members can view household payment methods" ON public.household_payment_methods;
CREATE POLICY "Members can view household payment methods"
  ON public.household_payment_methods FOR SELECT
  USING (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Members can link their payment methods" ON public.household_payment_methods;
CREATE POLICY "Members can link their payment methods"
  ON public.household_payment_methods FOR INSERT
  WITH CHECK (
    -- User must be a household member
    public.is_household_member(household_id, auth.uid())
    AND
    -- User must own the payment method
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_id
        AND pm.owner_user_id = auth.uid()
    )
  );

DROP POLICY IF EXISTS "Payment method owners or household owners can unlink" ON public.household_payment_methods;
CREATE POLICY "Payment method owners or household owners can unlink"
  ON public.household_payment_methods FOR DELETE
  USING (
    -- Payment method owner can always unlink their own methods
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_id
        AND pm.owner_user_id = auth.uid()
    )
    OR
    -- Household owners can unlink any payment method
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = household_payment_methods.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

-- ========================
-- CATEGORIES
-- ========================
DROP POLICY IF EXISTS "Household members can view categories" ON public.categories;
CREATE POLICY "Household members can view categories"
  ON public.categories FOR SELECT
  USING (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Household members can create categories" ON public.categories;
CREATE POLICY "Household members can create categories"
  ON public.categories FOR INSERT
  WITH CHECK (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Household members can update categories" ON public.categories;
CREATE POLICY "Household members can update categories"
  ON public.categories FOR UPDATE
  USING (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Household owners can delete categories" ON public.categories;
CREATE POLICY "Household owners can delete categories"
  ON public.categories FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

-- ========================
-- TRANSACTIONS
-- ========================
DROP POLICY IF EXISTS "Household members can view transactions" ON public.transactions;
CREATE POLICY "Household members can view transactions"
  ON public.transactions FOR SELECT
  USING (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Household members can create transactions" ON public.transactions;
CREATE POLICY "Household members can create transactions"
  ON public.transactions FOR INSERT
  WITH CHECK (
    -- Must be household member
    public.is_household_member(household_id, auth.uid())
    AND
    -- Payment method must be available to the household
    EXISTS (
      SELECT 1 FROM public.household_payment_methods hpm
      WHERE hpm.household_id = transactions.household_id
        AND hpm.payment_method_id = transactions.payment_method_id
    )
  );

DROP POLICY IF EXISTS "Transaction creators or household owners can update" ON public.transactions;
CREATE POLICY "Transaction creators or household owners can update"
  ON public.transactions FOR UPDATE
  USING (
    user_id = auth.uid()
    OR
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Transaction creators or household owners can delete" ON public.transactions;
CREATE POLICY "Transaction creators or household owners can delete"
  ON public.transactions FOR DELETE
  USING (
    user_id = auth.uid()
    OR
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

-- ========================
-- RECURRING PAYMENTS
-- ========================
DROP POLICY IF EXISTS "Household members can view recurring payments" ON public.recurring_payments;
CREATE POLICY "Household members can view recurring payments"
  ON public.recurring_payments FOR SELECT
  USING (
    public.is_household_member(household_id, auth.uid())
  );

DROP POLICY IF EXISTS "Household members can create recurring payments" ON public.recurring_payments;
CREATE POLICY "Household members can create recurring payments"
  ON public.recurring_payments FOR INSERT
  WITH CHECK (
    -- Must be household member
    public.is_household_member(household_id, auth.uid())
    AND
    -- Payment method must be available to the household
    EXISTS (
      SELECT 1 FROM public.household_payment_methods hpm
      WHERE hpm.household_id = recurring_payments.household_id
        AND hpm.payment_method_id = recurring_payments.payment_method_id
    )
  );

DROP POLICY IF EXISTS "Creators or household owners can update recurring payments" ON public.recurring_payments;
CREATE POLICY "Creators or household owners can update recurring payments"
  ON public.recurring_payments FOR UPDATE
  USING (
    user_id = auth.uid()
    OR
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Creators or household owners can delete recurring payments" ON public.recurring_payments;
CREATE POLICY "Creators or household owners can delete recurring payments"
  ON public.recurring_payments FOR DELETE
  USING (
    user_id = auth.uid()
    OR
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = auth.uid()
        AND hm.role = 'owner'
    )
  );
