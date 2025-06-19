CREATE TABLE product_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    manufacturer VARCHAR(100),
    amount INTEGER NOT NULL DEFAULT 0,
    price NUMERIC(10, 2) NOT NULL,
    group_id INTEGER NOT NULL,
    CONSTRAINT fk_product_group
        FOREIGN KEY (group_id)
        REFERENCES product_groups(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
