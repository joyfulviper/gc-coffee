CREATE TABLE if not exists products (
    product_id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);