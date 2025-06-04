-- DROP SCHEMA transaction_service_schema;

CREATE SCHEMA transaction_service_schema AUTHORIZATION postgres;
-- transaction_service_schema.transactions definition

-- Drop table

-- DROP TABLE transaction_service_schema.transactions;

CREATE TABLE transaction_service_schema.transactions (
	transaction_id uuid NOT NULL,
	account_id uuid NOT NULL,
	transaction_status varchar(20) NOT NULL,
	creation_date_time timestamp NOT NULL,
	last_updated timestamp NOT NULL,
	transaction_type varchar(50) NOT NULL,
	amount numeric(19, 2) NOT NULL,
	currency varchar(3) NOT NULL,
	merchant_category varchar(50) NOT NULL,
	transaction_channel varchar(20) NOT NULL,
	is_fraudulent bool NOT NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id)
);

-- Permissions

ALTER TABLE transaction_service_schema.transactions OWNER TO postgres;
GRANT ALL ON TABLE transaction_service_schema.transactions TO postgres;


-- transaction_service_schema.transactions foreign keys

ALTER TABLE transaction_service_schema.transactions ADD CONSTRAINT transactions_account_id_fkey FOREIGN KEY (account_id) REFERENCES account_service_schema.accounts(account_id);




-- Permissions

GRANT ALL ON SCHEMA transaction_service_schema TO postgres;