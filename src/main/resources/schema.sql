DROP TABLE IF EXISTS accounts;

CREATE TYPE ACCOUNT_TYPE AS ENUM ('CHECKING', 'SAVINGS', 'CREDIT', 'INVESTMENT');

CREATE TABLE accounts (
  id SERIAL PRIMARY KEY,
  user_id VARCHAR(50),
  account_type ACCOUNT_TYPE,
  account_number VARCHAR(50),
  routing_number VARCHAR(50),
  institution VARCHAR(50),
  investment_rate DECIMAL,
  starting_balance DECIMAL
);
