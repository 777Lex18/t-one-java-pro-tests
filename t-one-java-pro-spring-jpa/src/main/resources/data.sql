INSERT INTO users (username)
VALUES ('admin'), ('guest')
ON CONFLICT (username) DO NOTHING;

INSERT INTO products (account_number, balance, product_type, user_id)
SELECT
    'ACC-ADMIN-001',
    10000.00,
    'ACCOUNT',
    id
FROM users
WHERE username = 'admin'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO products (account_number, balance, product_type, user_id)
SELECT
    'CARD-GUEST-4242',
    11000.00,
    'ACCOUNT',
    id
FROM users
WHERE username = 'guest'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO products (account_number, balance, product_type, user_id)
SELECT
    'CARD-GUEST-777',
    500.50,
    'CARD',
    id
FROM users
WHERE username = 'guest'
ON CONFLICT (account_number) DO NOTHING;
