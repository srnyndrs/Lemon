-- USERS
CREATE TABLE IF NOT EXISTS public.users (
  id uuid PRIMARY KEY,
  username text,
  email text UNIQUE NOT NULL
);

-- HOUSEHOLDS
CREATE TABLE IF NOT EXISTS public.households (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL
);

-- Enum type for household member roles
DO $$ BEGIN
  CREATE TYPE household_member_type AS ENUM ('owner', 'member');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- HOUSEHOLD MEMBERS
CREATE TABLE IF NOT EXISTS public.household_members (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  role household_member_type DEFAULT 'member',
  UNIQUE (household_id, user_id)
);

-- Enum type for payment method types
DO $$ BEGIN
  CREATE TYPE payment_method_type AS ENUM ('cash', 'card', 'bank_account', 'other');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- PAYMENT METHODS
CREATE TABLE IF NOT EXISTS public.payment_methods (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  name text NOT NULL,
  icon text,
  color text,
  is_active boolean NOT NULL DEFAULT true,
  type payment_method_type NOT NULL DEFAULT 'other'
);

-- CATEGORIES
CREATE TABLE IF NOT EXISTS public.categories (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  name text NOT NULL,
  icon text,
  color text,
  UNIQUE (household_id, name)
);

-- Enum type for transaction types
DO $$ BEGIN
  CREATE TYPE transaction_type AS ENUM ('expense', 'income');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- Enum type for recurring payment frequencies
DO $$ BEGIN
  CREATE TYPE recurrence_period AS ENUM ('weekly', 'monthly', 'quarterly', 'yearly');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- RECURRING PAYMENTS
CREATE TABLE IF NOT EXISTS public.recurring_payments (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  category_id uuid REFERENCES public.categories(id) ON DELETE SET NULL,
  payment_method_id uuid NOT NULL REFERENCES public.payment_methods(id) ON DELETE CASCADE,
  
  -- Payment details
  title text NOT NULL,
  description text,
  amount numeric(12,2) NOT NULL CHECK (amount > 0),
  
  -- Recurrence settings
  recurrence_period recurrence_period NOT NULL,
  start_date date NOT NULL DEFAULT now(), -- when the recurring payment starts
  
  -- Next payment tracking
  next_due_date date NOT NULL, -- calculated next payment due date
  last_paid_date date, -- when it was last marked as paid
  
  -- Status
  is_active boolean DEFAULT true,

  -- Ensure next_due_date is after start_date
  CONSTRAINT valid_due_date CHECK (next_due_date >= start_date)
);

-- TRANSACTIONS
CREATE TABLE IF NOT EXISTS public.transactions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  payment_method_id uuid NOT NULL REFERENCES public.payment_methods(id) ON DELETE CASCADE,
  category_id uuid REFERENCES public.categories(id) ON DELETE SET NULL,
  recurring_payment_id uuid REFERENCES public.recurring_payments(id) ON DELETE SET NULL,
  type transaction_type NOT NULL,
  title text NOT NULL,
  description text,
  amount numeric(12,2) NOT NULL CHECK (amount > 0),
  transaction_date date NOT NULL DEFAULT now()
);

-- HOUSEHOLD PAYMENT METHODS
CREATE TABLE IF NOT EXISTS public.household_payment_methods (
  household_id uuid REFERENCES public.households(id) ON DELETE CASCADE,
  payment_method_id uuid REFERENCES public.payment_methods(id) ON DELETE CASCADE,
  PRIMARY KEY (household_id, payment_method_id)
);
