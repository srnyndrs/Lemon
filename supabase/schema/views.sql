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
SELECT DISTINCT ON (hpm.household_id, pm.id)
  hpm.household_id,
  pm.id as payment_method_id,
  pm.name,
  pm.icon,
  pm.color,
  pm.type,
  pm.owner_user_id,
  u.username as owner_username,
  pm.is_active
FROM public.household_payment_methods hpm
JOIN public.payment_methods pm ON hpm.payment_method_id = pm.id
JOIN public.users u ON pm.owner_user_id = u.id
WHERE pm.is_active = true
ORDER BY hpm.household_id, pm.id;

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
DROP VIEW IF EXISTS household_transactions_view;
CREATE OR REPLACE VIEW household_transactions_view AS
SELECT 
  t.id as transaction_id,
  t.household_id,
  EXTRACT(YEAR FROM t.transaction_date)::integer AS year,
  EXTRACT(MONTH FROM t.transaction_date)::integer AS month,
  c.name as category_name,
  c.icon as category_icon,
  c.color as category_color,
  t.payment_method_id as payment_method_id,
  COALESCE(pm.is_active, false) as payment_method_is_active,
  t.title,
  t.type,
  t.amount,
  t.transaction_date
FROM public.transactions t
LEFT JOIN public.categories c ON t.category_id = c.id
LEFT JOIN public.payment_methods pm ON t.payment_method_id = pm.id
ORDER BY t.transaction_date DESC;

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
-- View: household_monthly_expenses_view
-- Monthly expense totals by household
-- ==============================
DROP VIEW IF EXISTS household_monthly_expenses_view;
CREATE OR REPLACE VIEW household_monthly_expenses_view AS
SELECT
  household_id,
  EXTRACT(YEAR FROM transaction_date)::integer AS year,
  EXTRACT(MONTH FROM transaction_date)::integer AS month,
  SUM(amount) AS total_expenses
FROM public.transactions
WHERE type = 'expense'
GROUP BY household_id, 
  EXTRACT(YEAR FROM transaction_date),
  EXTRACT(MONTH FROM transaction_date)
ORDER BY household_id, year ASC, month ASC;

-- ==============================
-- View: household_summary_view
-- ==============================
DROP VIEW IF EXISTS household_summary_view;
CREATE OR REPLACE VIEW household_summary_view AS
SELECT
  t.household_id,
  EXTRACT(YEAR FROM t.transaction_date)::integer AS year,
  EXTRACT(MONTH FROM t.transaction_date)::integer AS month,
  c.id as category_id,
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN 'Income'
      WHEN c.name IS NULL THEN 'Uncategorized'
      ELSE c.name
    END, 'Uncategorized'
  ) AS category_name,
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN 'income'
      WHEN c.icon IS NULL THEN 'Uncategorized'
      ELSE c.icon
    END, 'Uncategorized'
  ) AS category_icon,
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN '#2ecc40'
      WHEN c.color IS NULL THEN '#cccccc'
      ELSE c.color
    END, '#cccccc'
  ) AS category_color,
  SUM(t.amount) AS total_amount
FROM public.transactions t
LEFT JOIN public.categories c ON t.category_id = c.id
WHERE DATE_TRUNC('month', t.transaction_date) = DATE_TRUNC('month', CURRENT_DATE)
GROUP BY t.household_id,
  EXTRACT(YEAR FROM t.transaction_date),
  EXTRACT(MONTH FROM t.transaction_date),
  c.id,
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN 'Income'
      WHEN c.name IS NULL THEN 'Uncategorized'
      ELSE c.name
    END, 'Uncategorized'
  ),
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN 'income'
      WHEN c.icon IS NULL THEN 'Uncategorized'
      ELSE c.icon
    END, 'Uncategorized'
  ),
  COALESCE(
    CASE 
      WHEN t.type = 'income' THEN '#2ecc40'
      WHEN c.color IS NULL THEN '#cccccc'
      ELSE c.color
    END, '#cccccc'
  )
ORDER BY t.household_id, year, month, category_name;

GRANT SELECT ON user_households TO authenticated;
GRANT SELECT ON household_payment_methods_view TO authenticated;
GRANT SELECT ON household_categories_view TO authenticated;
GRANT SELECT ON household_transactions_view TO authenticated;
GRANT SELECT ON household_members_view TO authenticated;
GRANT SELECT ON household_monthly_expenses_view TO authenticated;
GRANT SELECT ON household_summary_view TO authenticated;

-- ==============================
-- View: household_details_view
-- Household details with members aggregated
-- ==============================
CREATE OR REPLACE VIEW household_details_view AS
SELECT
    h.id AS household_id,
    h.name AS household_name,
    json_agg(
        json_build_object(
            'user_id', u.id,
            'username', u.username,
            'email', u.email,
            'role', hm.role
        )
    ) AS members
FROM
    public.households h
JOIN
    public.household_members hm ON h.id = hm.household_id
JOIN
    public.users u ON hm.user_id = u.id
GROUP BY
    h.id, h.name;

GRANT SELECT ON household_details_view TO authenticated;

-- ==============================
-- View: users_view
-- All users with their id and username
-- ==============================
CREATE OR REPLACE VIEW users_view AS
SELECT
    u.id,
    u.username
FROM
    public.users u;

GRANT SELECT ON users_view TO authenticated;

-- ==============================
-- View: household_monthly_income_view
-- Monthly income totals by household
-- ==============================
DROP VIEW IF EXISTS household_monthly_income_view;
CREATE OR REPLACE VIEW household_monthly_income_view AS
SELECT
  household_id,
  EXTRACT(YEAR FROM transaction_date)::integer AS year,
  EXTRACT(MONTH FROM transaction_date)::integer AS month,
  SUM(amount) AS total_income
FROM public.transactions
WHERE type = 'income'
GROUP BY household_id,
  EXTRACT(YEAR FROM transaction_date),
  EXTRACT(MONTH FROM transaction_date)
ORDER BY household_id, year DESC, month DESC;

GRANT SELECT ON household_monthly_income_view TO authenticated;
