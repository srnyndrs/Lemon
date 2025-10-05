-- ========================
-- USERS
-- ========================
CREATE POLICY "Users can view all user records"
  ON public.users FOR SELECT
  USING (true);

CREATE POLICY "Users can insert their own user record"
  ON public.users FOR INSERT 
  WITH CHECK ((SELECT auth.uid()) = id);

CREATE POLICY "Users can update their own user record" 
  ON public.users FOR UPDATE
  USING ((SELECT auth.uid()) = id);

-- ========================
-- HOUSEHOLDS
-- ========================
CREATE POLICY "Users can view households they belong to"
  ON public.households FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = households.id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Authenticated users can create households"
  ON public.households FOR INSERT
  WITH CHECK (auth.uid() IS NOT NULL);

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
CREATE POLICY "Members can view members of their household"
  ON public.household_members FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = household_members.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Only owners can add members"
  ON public.household_members FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = household_members.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

CREATE POLICY "Only owners can remove members"
  ON public.household_members FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = household_members.household_id
        AND hm.user_id = (SELECT auth.uid())
        AND hm.role = 'owner'
    )
  );

-- ========================
-- PAYMENT METHODS
-- ========================
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

CREATE POLICY "Users can create their own payment methods"
  ON public.payment_methods FOR INSERT
  WITH CHECK (owner_user_id = (SELECT auth.uid()));

CREATE POLICY "Only owner can update payment method"
  ON public.payment_methods FOR UPDATE
  USING (owner_user_id = (SELECT auth.uid()));

CREATE POLICY "Only owner can delete payment method"
  ON public.payment_methods FOR DELETE
  USING (owner_user_id = (SELECT auth.uid()));

-- ========================
-- PAYMENT METHOD SHARES
-- ========================
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

CREATE POLICY "Only owner can add shares"
  ON public.payment_method_shares FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.payment_methods pm
      WHERE pm.id = payment_method_shares.payment_method_id
        AND pm.owner_user_id = (SELECT auth.uid())
    )
  );

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
CREATE POLICY "Household members can view transactions"
  ON public.transactions FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Household members can insert transactions"
  ON public.transactions FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = transactions.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

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
CREATE POLICY "Household members can view their household categories"
  ON public.categories FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Household members can create categories for their household"
  ON public.categories FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Household members can update their household categories"
  ON public.categories FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = categories.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

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
CREATE POLICY "Household members can view recurring payments"
  ON public.recurring_payments FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

CREATE POLICY "Household members can create recurring payments"
  ON public.recurring_payments FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM public.household_members hm
      WHERE hm.household_id = recurring_payments.household_id
        AND hm.user_id = (SELECT auth.uid())
    )
  );

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
