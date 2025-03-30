DROP TABLE IF EXISTS exchange_rates;
DROP TABLE IF EXISTS currencies;

CREATE TABLE currencies (
    id SMALLSERIAL PRIMARY KEY NOT NULL,
    code VARCHAR(3) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    sign VARCHAR(2) NOT NULL
);

CREATE TABLE exchange_rates (
    id SERIAL PRIMARY KEY NOT NULL,
    base_currency_id SMALLINT NOT NULL REFERENCES currencies (id),
    target_currency_id SMALLINT NOT NULL REFERENCES currencies (id),
    rate DECIMAL(6) NOT NULL,
    UNIQUE (base_currency_id, target_currency_id)
);

INSERT INTO currencies (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$')