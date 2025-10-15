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
  WITH CHECK ((SELECT auth.uid()) = id);

DROP POLICY IF EXISTS "Users can update their own user record" ON public.users;
CREATE POLICY "Users can update their own user record" 
  ON public.users FOR UPDATE
  USING ((SELECT auth.uid()) = id);

-- ========================
-- HOUSEHOLDS
-- ========================
DROP POLICY IF EXISTS "Users can view households they belong to" ON public.households;
CREATE POLICY "Users can view households they belong to"
  ON public.households FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = households.id
        AND hm.user_id = (SELECT auth.uid())
    )
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
      WHERE hm.household_id = households.id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Only household owners can delete household" ON public.households;
CREATE POLICY "Only household owners can delete household"
  ON public.households FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = households.id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

-- ========================
-- HOUSEHOLD MEMBERS
-- ========================
DROP POLICY IF EXISTS "Users can view household members where they are also members" ON public.household_members;
CREATE POLICY "Users can view household members where they are also members"
  ON public.household_members FOR SELECT
  USING (
    -- Users can only see their own membership records
    user_id = (SELECT auth.uid())
  );

DROP POLICY IF EXISTS "Only owners can add members or self-assign as owner" ON public.household_members;
CREATE POLICY "Only owners can add members or self-assign as owner"
  ON public.household_members FOR INSERT
  WITH CHECK (
    -- Allow users to add themselves as owner (initial household creation)
    (household_members.user_id = (SELECT auth.uid()) AND household_members.role = 'owner')
    OR
    -- Allow if user is already an owner in households they own
    household_members.household_id IN (
      SELECT hm.household_id 
      FROM public.household_members hm 
      WHERE hm.user_id = (SELECT auth.uid()) 
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Only owners can remove members" ON public.household_members;
CREATE POLICY "Only owners can remove members"
  ON public.household_members FOR DELETE
  USING (
    -- Allow if user is an owner in households they own
    household_members.household_id IN (
      SELECT hm.household_id 
      FROM public.household_members hm 
      WHERE hm.user_id = (SELECT auth.uid()) 
        AND hm.role = 'owner'
    )
  );

-- ========================
-- PAYMENT METHODS
-- ========================
DROP POLICY IF EXISTS "Users can view payment methods shared with them" ON public.payment_methods;
CREATE POLICY "Users can view payment methods shared with them"
  ON public.payment_methods FOR SELECT
  USING (
    owner_user_id = (SELECT auth.uid())
    OR EXISTS (
      SELECT 1 FROM public.payment_method_shares pms
      WHERE pms.payment_method_id = payment_methods.id
        AND pms.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Users can create their own payment methods" ON public.payment_methods;
CREATE POLICY "Users can create their own payment methods"
  ON public.payment_methods FOR INSERT
  WITH CHECK (owner_user_id = (SELECT auth.uid()));

DROP POLICY IF EXISTS "Only owner can update payment method" ON public.payment_methods;
CREATE POLICY "Only owner can update payment method"
  ON public.payment_methods FOR UPDATE
  USING (owner_user_id = (SELECT auth.uid()));

DROP POLICY IF EXISTS "Only owner can delete payment method" ON public.payment_methods;
CREATE POLICY "Only owner can delete payment method"
  ON public.payment_methods FOR DELETE
  USING (owner_user_id = (SELECT auth.uid()));

-- ========================
-- PAYMENT METHOD SHARES
-- ========================
DROP POLICY IF EXISTS "Users can view shares for payment methods they have access to" ON public.payment_method_shares;
CREATE POLICY "Users can view shares for payment methods they have access to"
  ON public.payment_method_shares FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_shares.payment_method_id
        AND (
          pm.owner_user_id = (SELECT auth.uid())
          OR EXISTS (
            SELECT 1 FROM public.payment_method_shares pms
            WHERE pms.payment_method_id = pm.id
              AND pms.user_id = (SELECT auth.uid())
          )
        )
    )
  );

DROP POLICY IF EXISTS "Only owner can add shares" ON public.payment_method_shares;
CREATE POLICY "Only owner can add shares"
  ON public.payment_method_shares FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_shares.payment_method_id
        AND pm.owner_user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Only owner can remove shares" ON public.payment_method_shares;
CREATE POLICY "Only owner can remove shares"
  ON public.payment_method_shares FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_shares.payment_method_id
        AND pm.owner_user_id = (SELECT auth.uid())
    )
  );

-- ========================
-- TRANSACTIONS
-- ========================
DROP POLICY IF EXISTS "Household members can view transactions" ON public.transactions;
CREATE POLICY "Household members can view transactions"
  ON public.transactions FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Household members can insert transactions" ON public.transactions;
CREATE POLICY "Household members can insert transactions"
  ON public.transactions FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Users can update their own transactions or household owners any" ON public.transactions;
CREATE POLICY "Users can update their own transactions or household owners any"
  ON public.transactions FOR UPDATE
  USING (
    user_id = (SELECT auth.uid())
    OR EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Users can delete their own transactions or household owners any" ON public.transactions;
CREATE POLICY "Users can delete their own transactions or household owners any"
  ON public.transactions FOR DELETE
  USING (
    user_id = (SELECT auth.uid())
    OR EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

-- ========================
-- HOUSEHOLD PAYMENT METHODS
-- ========================
DROP POLICY IF EXISTS "Members can view linked payment methods" ON public.household_payment_methods;
CREATE POLICY "Members can view linked payment methods"
  ON public.household_payment_methods
  FOR SELECT
  USING (
    EXISTS (
        SELECT 1
        FROM public.household_members hm
        WHERE hm.household_id = household_payment_methods.household_id
        AND hm.user_id = auth.uid()
    )
  );

DROP POLICY IF EXISTS "Owners can link their payment methods to their households" ON public.household_payment_methods;
CREATE POLICY "Owners can link their payment methods to their households"
  ON public.household_payment_methods
  FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1
      FROM public.payment_methods pm
      WHERE pm.id = household_payment_methods.payment_method_id
      AND pm.owner_user_id = auth.uid()
    )
  AND
    EXISTS (
      SELECT 1
      FROM public.household_members hm
      WHERE hm.household_id = household_payment_methods.household_id
      AND hm.user_id = auth.uid()
    )
  );

DROP POLICY IF EXISTS "Owners or household owners can unlink payment methods" ON public.household_payment_methods;
CREATE POLICY "Owners or household owners can unlink payment methods"
  ON public.household_payment_methods
  FOR DELETE
  USING (
    EXISTS (
      SELECT 1
      FROM public.payment_methods pm
      WHERE pm.id = household_payment_methods.payment_method_id
      AND pm.owner_user_id = auth.uid()
    )
  OR
    EXISTS (
      SELECT 1
      FROM public.household_members hm
      WHERE hm.household_id = household_payment_methods.household_id
      AND hm.user_id = auth.uid()
      AND hm.role = 'owner'
    )
  );

-- ========================
-- CATEGORIES
-- ========================
DROP POLICY IF EXISTS "Household members can view their household categories" ON public.categories;
CREATE POLICY "Household members can view their household categories"
  ON public.categories FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Household members can create categories for their household" ON public.categories;
CREATE POLICY "Household members can create categories for their household"
  ON public.categories FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Household members can update their household categories" ON public.categories;
CREATE POLICY "Household members can update their household categories"
  ON public.categories FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Household owners can delete categories" ON public.categories;
CREATE POLICY "Household owners can delete categories"
  ON public.categories FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
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
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Household members can create recurring payments" ON public.recurring_payments;
CREATE POLICY "Household members can create recurring payments"
  ON public.recurring_payments FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

DROP POLICY IF EXISTS "Users can update their own recurring payments or household owners any" ON public.recurring_payments;
CREATE POLICY "Users can update their own recurring payments or household owners any"
  ON public.recurring_payments FOR UPDATE
  USING (
    user_id = (SELECT auth.uid())
    OR EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

DROP POLICY IF EXISTS "Users can delete their own recurring payments or household owners any" ON public.recurring_payments;
CREATE POLICY "Users can delete their own recurring payments or household owners any"
  ON public.recurring_payments FOR DELETE
  USING (
    user_id = (SELECT auth.uid())
    OR EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );