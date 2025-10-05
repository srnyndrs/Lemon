-- USERS (main auth table)
CREATE TABLE public.users (
  id uuid PRIMARY KEY,
  email text UNIQUE NOT NULL,
  username text,
  created_at timestamptz DEFAULT now()
);

-- HOUSEHOLDS
CREATE TABLE public.households (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL,
  created_at timestamptz DEFAULT now()
);

-- Enum type for household member roles
DO $$ BEGIN
  CREATE TYPE household_member_type AS ENUM ('owner', 'member');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- HOUSEHOLD MEMBERS (profiles <-> households)
CREATE TABLE public.household_members (
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
CREATE TABLE public.payment_methods (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_user_id uuid REFERENCES public.users(id) ON DELETE CASCADE,
  name text NOT NULL,
  icon text, -- icon identifier
  color text, -- hex color code for UI
  type payment_method_type NOT NULL
);

-- PAYMENT METHOD SHARES
CREATE TABLE public.payment_method_shares (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  payment_method_id uuid NOT NULL REFERENCES public.payment_methods(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  UNIQUE (payment_method_id, user_id)
);

-- Enum type for transaction types
DO $$ BEGIN
  CREATE TYPE transaction_type AS ENUM ('expense', 'income');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- TRANSACTIONS
CREATE TABLE public.transactions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  category_id uuid REFERENCES public.categories(id) ON DELETE SET NULL,
  payment_method_id uuid REFERENCES public.payment_methods(id) ON DELETE SET NULL,
  recurring_payment_id uuid REFERENCES public.recurring_payments(id) ON DELETE SET NULL,
  type transaction_type NOT NULL,
  amount numeric(12,2) NOT NULL,
  description text,
  transaction_date date NOT NULL DEFAULT now(), -- when the transaction actually occurred
  created_at timestamptz DEFAULT now() -- when the record was created
);

-- HOUSEHOLD PAYMENT METHODS (HOUSEHOLD <-> PAYMENT METHODS)
CREATE TABLE public.household_payment_methods (
  household_id uuid REFERENCES public.households(id) ON DELETE CASCADE,
  payment_method_id uuid REFERENCES public.payment_methods(id) ON DELETE CASCADE,
  PRIMARY KEY (household_id, payment_method_id)
);

-- CATEGORIES (for organizing transactions and recurring payments)
CREATE TABLE public.categories (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  name text NOT NULL,
  icon text, -- icon identifier
  color text, -- hex color code for UI
  created_at timestamptz DEFAULT now(),
  UNIQUE (household_id, name) -- unique category name per household
);

-- Enum type for recurring payment frequencies
DO $$ BEGIN
  CREATE TYPE recurrence_period AS ENUM ('weekly', 'monthly', 'quarterly', 'yearly');
EXCEPTION
  WHEN duplicate_object THEN null;
END $$;

-- RECURRING PAYMENTS (subscriptions, bills, etc.)
CREATE TABLE public.recurring_payments (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  household_id uuid NOT NULL REFERENCES public.households(id) ON DELETE CASCADE,
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE, -- who created it
  category_id uuid REFERENCES public.categories(id) ON DELETE SET NULL,
  payment_method_id uuid REFERENCES public.payment_methods(id) ON DELETE SET NULL,
  
  -- Payment details
  name text NOT NULL, -- e.g., "Spotify Premium", "Electric Bill"
  description text,
  amount numeric(12,2) NOT NULL,
  
  -- Recurrence settings
  recurrence_period recurrence_period NOT NULL,
  start_date date NOT NULL, -- when the recurring payment starts
  
  -- Next payment tracking
  next_due_date date NOT NULL, -- calculated next payment due date
  last_paid_date date, -- when it was last marked as paid
  
  -- Status
  is_active boolean DEFAULT true,
  
  created_at timestamptz DEFAULT now()
);
