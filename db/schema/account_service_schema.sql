-- DROP SCHEMA account_service_schema;

CREATE SCHEMA account_service_schema AUTHORIZATION postgres;
-- account_service_schema.accounts definition

-- Drop table

-- DROP TABLE account_service_schema.accounts;

CREATE TABLE account_service_schema.accounts (
	account_id uuid NOT NULL, -- Unique identifier for the account (UUID)
	account_number varchar(255) NOT NULL, -- Account number (String)
	account_type varchar(20) NOT NULL, -- Type of account (e.g., SAVINGS, CHECKING)
	account_status varchar(20) NOT NULL, -- Status of the account (e.g., ACTIVE, INACTIVE)
	account_holder_name varchar(255) NOT NULL, -- Name of the account holder
	opening_date date NOT NULL, -- Date when the account was opened
	currency varchar(3) NOT NULL, -- Currency of the account (e.g., GBP, USD)
	balance numeric(19, 2) NOT NULL, -- Current balance of the account
	interest_rate numeric(5, 2) NOT NULL, -- Interest rate applied to the account
	credit_limit numeric(19, 2) NOT NULL, -- Credit limit for the account
	CONSTRAINT accounts_pkey PRIMARY KEY (account_id)
);
COMMENT ON TABLE account_service_schema.accounts IS 'Table to store account information';

-- Column comments

COMMENT ON COLUMN account_service_schema.accounts.account_id IS 'Unique identifier for the account (UUID)';
COMMENT ON COLUMN account_service_schema.accounts.account_number IS 'Account number (String)';
COMMENT ON COLUMN account_service_schema.accounts.account_type IS 'Type of account (e.g., SAVINGS, CHECKING)';
COMMENT ON COLUMN account_service_schema.accounts.account_status IS 'Status of the account (e.g., ACTIVE, INACTIVE)';
COMMENT ON COLUMN account_service_schema.accounts.account_holder_name IS 'Name of the account holder';
COMMENT ON COLUMN account_service_schema.accounts.opening_date IS 'Date when the account was opened';
COMMENT ON COLUMN account_service_schema.accounts.currency IS 'Currency of the account (e.g., GBP, USD)';
COMMENT ON COLUMN account_service_schema.accounts.balance IS 'Current balance of the account';
COMMENT ON COLUMN account_service_schema.accounts.interest_rate IS 'Interest rate applied to the account';
COMMENT ON COLUMN account_service_schema.accounts.credit_limit IS 'Credit limit for the account';

-- Permissions

ALTER TABLE account_service_schema.accounts OWNER TO postgres;
GRANT ALL ON TABLE account_service_schema.accounts TO postgres;




-- Permissions

GRANT ALL ON SCHEMA account_service_schema TO postgres;