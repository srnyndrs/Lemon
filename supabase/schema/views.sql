-- ==============================
-- View: user_households
-- All households a user belongs to with their role
-- ==============================
DROP VIEW IF EXISTS public.user_households;
CREATE VIEW public.user_households AS
SELECT
  u.id as user_id,
  u.email,
  u.username,
  hm.household_id,
  h.name as household_name,
  hm.role as household_role
FROM
  public.users u
  LEFT JOIN public.household_members hm ON hm.user_id = u.id
  LEFT JOIN public.households h ON h.id = hm.household_id
ORDER BY
  u.email,
  h.name;

-- ==============================
-- View: household_payment_methods_view
-- All payment methods available to each household
-- ==============================
CREATE OR REPLACE VIEW household_payment_methods_view AS
SELECT 
  hpm.household_id,
  pm.id as payment_method_id,
  pm.name,
  pm.icon,
  pm.color,
  pm.type,
  pm.owner_user_id,
  u.username as owner_username
FROM public.household_payment_methods hpm
JOIN public.payment_methods pm ON hpm.payment_method_id = pm.id
JOIN public.users u ON pm.owner_user_id = u.id;

-- ==============================
-- View: household_categories_view  
-- All categories for each household
-- ==============================
CREATE OR REPLACE VIEW household_categories_view AS
SELECT 
  c.household_id,
  c.id as category_id,
  c.name,
  c.icon,
  c.color,
  -- Count of transactions using this category
  COALESCE(t.transaction_count, 0) as transaction_count
FROM public.categories c
LEFT JOIN (
  SELECT 
    category_id,
    COUNT(*) as transaction_count
  FROM public.transactions
  WHERE category_id IS NOT NULL
  GROUP BY category_id
) t ON c.id = t.category_id;

-- ==============================
-- View: household_transactions_view
-- All transactions with related data
-- ==============================
CREATE OR REPLACE VIEW household_transactions_view AS
SELECT 
  t.id as transaction_id,
  t.household_id,
  t.user_id,
  u.username,
  t.payment_method_id,
  pm.name as payment_method_name,
  pm.icon as payment_method_icon,
  pm.color as payment_method_color,
  t.category_id,
  c.name as category_name,
  c.icon as category_icon,
  c.color as category_color,
  t.recurring_payment_id,
  t.type,
  t.title,
  t.description,
  t.amount,
  t.transaction_date
FROM public.transactions t
JOIN public.users u ON t.user_id = u.id
JOIN public.payment_methods pm ON t.payment_method_id = pm.id
LEFT JOIN public.categories c ON t.category_id = c.id;

-- ==============================
-- View: household_recurring_payments_view
-- All recurring payments with related data
-- ==============================
CREATE OR REPLACE VIEW household_recurring_payments_view AS
SELECT 
  rp.id as recurring_payment_id,
  rp.household_id,
  rp.user_id,
  u.username,
  rp.payment_method_id,
  pm.name as payment_method_name,
  pm.icon as payment_method_icon,
  pm.color as payment_method_color,
  rp.category_id,
  c.name as category_name,
  c.icon as category_icon,
  c.color as category_color,
  rp.title,
  rp.description,
  rp.amount,
  rp.recurrence_period,
  rp.start_date,
  rp.next_due_date,
  rp.last_paid_date,
  rp.is_active,
  -- Calculate if payment is overdue
  CASE 
    WHEN rp.next_due_date < CURRENT_DATE AND rp.is_active THEN true
    ELSE false
  END as is_overdue,
  -- Days until/since due date
  rp.next_due_date - CURRENT_DATE as days_until_due
FROM public.recurring_payments rp
JOIN public.users u ON rp.user_id = u.id
JOIN public.payment_methods pm ON rp.payment_method_id = pm.id
LEFT JOIN public.categories c ON rp.category_id = c.id;

-- ==============================
-- View: household_members_view
-- All household members with user details
-- ==============================
CREATE OR REPLACE VIEW household_members_view AS
SELECT 
  hm.id as membership_id,
  hm.household_id,
  h.name as household_name,
  hm.user_id,
  u.username,
  u.email,
  hm.role
FROM public.household_members hm
JOIN public.households h ON hm.household_id = h.id
JOIN public.users u ON hm.user_id = u.id;

-- ==============================
-- View: household_summary_view
-- Summary statistics for each household
-- ==============================
CREATE OR REPLACE VIEW household_summary_view AS
SELECT 
  h.id as household_id,
  h.name as household_name,
  -- Member count
  COALESCE(members.member_count, 0) as member_count,
  -- Transaction counts and totals
  COALESCE(trans_stats.total_transactions, 0) as total_transactions,
  COALESCE(trans_stats.total_expenses, 0) as total_expenses,
  COALESCE(trans_stats.total_income, 0) as total_income,
  COALESCE(trans_stats.net_amount, 0) as net_amount,
  -- This month's totals
  COALESCE(month_stats.month_expenses, 0) as current_month_expenses,
  COALESCE(month_stats.month_income, 0) as current_month_income,
  -- Category and payment method counts
  COALESCE(cats.category_count, 0) as category_count,
  COALESCE(pms.payment_method_count, 0) as payment_method_count,
  -- Active recurring payments
  COALESCE(recurring.active_recurring_count, 0) as active_recurring_count,
  COALESCE(recurring.overdue_recurring_count, 0) as overdue_recurring_count
FROM public.households h
LEFT JOIN (
  SELECT household_id, COUNT(*) as member_count
  FROM public.household_members
  GROUP BY household_id
) members ON h.id = members.household_id
LEFT JOIN (
  SELECT 
    household_id,
    COUNT(*) as total_transactions,
    SUM(CASE WHEN type = 'expense' THEN amount ELSE 0 END) as total_expenses,
    SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END) as total_income,
    SUM(CASE WHEN type = 'income' THEN amount ELSE -amount END) as net_amount
  FROM public.transactions
  GROUP BY household_id
) trans_stats ON h.id = trans_stats.household_id
LEFT JOIN (
  SELECT 
    household_id,
    SUM(CASE WHEN type = 'expense' THEN amount ELSE 0 END) as month_expenses,
    SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END) as month_income
  FROM public.transactions
  WHERE DATE_TRUNC('month', transaction_date) = DATE_TRUNC('month', CURRENT_DATE)
  GROUP BY household_id
) month_stats ON h.id = month_stats.household_id
LEFT JOIN (
  SELECT household_id, COUNT(*) as category_count
  FROM public.categories
  GROUP BY household_id
) cats ON h.id = cats.household_id
LEFT JOIN (
  SELECT household_id, COUNT(*) as payment_method_count
  FROM public.household_payment_methods
  GROUP BY household_id
) pms ON h.id = pms.household_id
LEFT JOIN (
  SELECT 
    household_id,
    COUNT(*) FILTER (WHERE is_active = true) as active_recurring_count,
    COUNT(*) FILTER (WHERE is_active = true AND next_due_date < CURRENT_DATE) as overdue_recurring_count
  FROM public.recurring_payments
  GROUP BY household_id
) recurring ON h.id = recurring.household_id;

GRANT SELECT ON user_households TO authenticated;
GRANT SELECT ON household_payment_methods_view TO authenticated;
GRANT SELECT ON household_categories_view TO authenticated;
GRANT SELECT ON household_transactions_view TO authenticated;
GRANT SELECT ON household_recurring_payments_view TO authenticated;
GRANT SELECT ON household_members_view TO authenticated;
GRANT SELECT ON household_summary_view TO authenticated;
