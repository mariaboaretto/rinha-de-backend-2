-- Initial scripts
CREATE TABLE IF NOT EXISTS customers(
	customer_id SERIAL PRIMARY KEY,
	customer_name TEXT,
	customer_limit INTEGER,
	customer_balance INTEGER DEFAULT 0 CONSTRAINT total_balance_check CHECK (customer_balance >= (customer_limit * -1))
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(customer_id),
    transaction_description VARCHAR(10),
	transaction_value INTEGER,
	transaction_type CHAR,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserting default values
DO $$
BEGIN
  INSERT INTO customers(customer_name, customer_limit)
  VALUES
    ('o barato sai caro', 1000 * 100),
    ('zan corp ltda', 800 * 100),
    ('les cruders', 10000 * 100),
    ('padaria joia de cocaia', 100000 * 100),
    ('kid mais', 5000 * 100);
END; $$
