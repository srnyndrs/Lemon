-- ==============================
-- USERS TABLE INDEXES
-- ==============================
-- Primary key (id) already has index
CREATE INDEX IF NOT EXISTS idx_users_email ON public.users(email);

-- ==============================
-- HOUSEHOLD MEMBERS TABLE INDEXES
-- ==============================
CREATE INDEX IF NOT EXISTS idx_household_members_household_id ON public.household_members(household_id);
CREATE INDEX IF NOT EXISTS idx_household_members_user_id ON public.household_members(user_id);
-- Composite index for common queries (user's households with role)
CREATE INDEX IF NOT EXISTS idx_household_members_user_household ON public.household_members(user_id, household_id);
-- Index for role-based queries (finding owners)
CREATE INDEX IF NOT EXISTS idx_household_members_household_role ON public.household_members(household_id, role);

-- ==============================
-- PAYMENT METHODS TABLE INDEXES
-- ==============================
CREATE INDEX IF NOT EXISTS idx_payment_methods_owner_user_id ON public.payment_methods(owner_user_id);
-- Index for payment method type filtering
CREATE INDEX IF NOT EXISTS idx_payment_methods_type ON public.payment_methods(type);

-- ==============================
-- HOUSEHOLD PAYMENT METHODS TABLE INDEXES
-- ==============================
CREATE INDEX IF NOT EXISTS idx_household_payment_methods_household_id ON public.household_payment_methods(household_id);
CREATE INDEX IF NOT EXISTS idx_household_payment_methods_payment_method_id ON public.household_payment_methods(payment_method_id);

-- ==============================
-- CATEGORIES TABLE INDEXES
-- ==============================
CREATE INDEX IF NOT EXISTS idx_categories_household_id ON public.categories(household_id);
-- Unique constraint already creates index on (household_id, name)

-- ==============================
-- TRANSACTIONS TABLE INDEXES
-- ==============================
-- Core foreign key indexes
CREATE INDEX IF NOT EXISTS idx_transactions_household_id ON public.transactions(household_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON public.transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_payment_method_id ON public.transactions(payment_method_id);
CREATE INDEX IF NOT EXISTS idx_transactions_category_id ON public.transactions(category_id);

-- Date-based indexes for filtering and sorting
CREATE INDEX IF NOT EXISTS idx_transactions_transaction_date ON public.transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_transactions_date_desc ON public.transactions(transaction_date DESC);

-- Composite indexes for common query patterns
-- Household transactions by date (most common query)
CREATE INDEX IF NOT EXISTS idx_transactions_household_date ON public.transactions(household_id, transaction_date DESC);
-- User transactions by date
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON public.transactions(user_id, transaction_date DESC);
-- Household transactions by type and date
CREATE INDEX IF NOT EXISTS idx_transactions_household_type_date ON public.transactions(household_id, type, transaction_date DESC);

-- Filtering by transaction type
CREATE INDEX IF NOT EXISTS idx_transactions_type ON public.transactions(type);

-- Index only recent transactions (from 2023 onwards for performance)
CREATE INDEX IF NOT EXISTS idx_transactions_recent ON public.transactions(household_id, transaction_date DESC) 
WHERE transaction_date >= '2023-01-01'::date;

-- Index only expense transactions (often filtered separately)
CREATE INDEX IF NOT EXISTS idx_transactions_expenses ON public.transactions(household_id, transaction_date DESC, amount) 
WHERE type = 'expense';

-- Index only income transactions
CREATE INDEX IF NOT EXISTS idx_transactions_income ON public.transactions(household_id, transaction_date DESC, amount) 
WHERE type = 'income';

-- ==============================
-- COVERING INDEXES FOR VIEW PERFORMANCE
-- ==============================
-- For household_transactions_view - include commonly selected columns
CREATE INDEX IF NOT EXISTS idx_transactions_view_covering ON public.transactions(household_id, transaction_date DESC) 
INCLUDE (user_id, payment_method_id, category_id, type, title, amount, description);

-- For household_summary_view aggregations
CREATE INDEX IF NOT EXISTS idx_transactions_summary_covering ON public.transactions(household_id, type) 
INCLUDE (amount, transaction_date);
