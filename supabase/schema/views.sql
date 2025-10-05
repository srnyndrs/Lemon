-- ==============================
-- View: user_with_households
-- User information with one row per user-household combination
-- ==============================
DROP VIEW IF EXISTS public.user_with_households;
CREATE VIEW public.user_with_households AS
SELECT
  u.id as user_id,
  u.email,
  u.username,
  hm.household_id,
  h.name as household_name,
  hm.role as household_role,
  h.created_at as household_created_at
FROM
  public.users u
  LEFT JOIN public.household_members hm ON hm.user_id = u.id
  LEFT JOIN public.households h ON h.id = hm.household_id
ORDER BY
  u.email,
  h.name;

-- ==============================
-- View: monthly_household_category_stats
-- Monthly expense statistics for households grouped by categories
-- ==============================
CREATE OR REPLACE VIEW public.monthly_household_category_stats AS
SELECT 
  t.household_id,
  h.name as household_name,
  EXTRACT(YEAR FROM t.transaction_date) as year,
  EXTRACT(MONTH FROM t.transaction_date) as month,
  TO_CHAR(t.transaction_date, 'YYYY-MM') as year_month,
  t.category_id,
  COALESCE(c.name, 'Uncategorized') as category_name,
  c.icon as category_icon,
  c.color as category_color,
  COUNT(t.id) as transaction_count,
  SUM(t.amount) as total_amount,
  AVG(t.amount) as average_amount,
  MIN(t.amount) as min_amount,
  MAX(t.amount) as max_amount
FROM public.transactions t
JOIN public.households h ON h.id = t.household_id
LEFT JOIN public.categories c ON c.id = t.category_id
WHERE t.type = 'expense'
GROUP BY 
  t.household_id,
  h.name,
  EXTRACT(YEAR FROM t.transaction_date),
  EXTRACT(MONTH FROM t.transaction_date),
  TO_CHAR(t.transaction_date, 'YYYY-MM'),
  t.category_id,
  c.name,
  c.icon,
  c.color
ORDER BY 
  t.household_id,
  year DESC,
  month DESC,
  total_amount DESC;
