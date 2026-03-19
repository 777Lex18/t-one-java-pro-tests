--DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    product_type VARCHAR(20) NOT NULL CHECK (product_type IN ('ACCOUNT', 'CARD')),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_product_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_products_user_id ON products(user_id);