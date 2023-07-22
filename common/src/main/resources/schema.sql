CREATE TABLE products (
    product_id Binary(16) Primary Key,
    product_name VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price bigint NOT NULL,
    description VARCHAR(500) NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL
);

CREATE TABLE orders (
    order_id binary(16) Primary Key,
    email varchar(50) NOT NULL,
    address varchar(200) NOT NULL,
    postcode varchar(200) NOT NULL,
    order_status varchar(50) NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL
);

CREATE TABLE order_items (
    seq bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id binary(16) NOT NULL,
    product_id binary(16) NOT NULL,
    category varchar(50) NOT NULL,
    price bigint NOT NULL,
    quantity int NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    Index (order_id),
    CONSTRAINT fk_order_items_orders FOREIGN KEY (order_id) REFERENCES orders (order_id) on delete cascade,
    constraint fk_order_items_products foreign key (product_id) references products (product_id)
)