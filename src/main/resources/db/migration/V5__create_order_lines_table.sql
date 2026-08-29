CREATE TABLE order_lines
(
    id         UUID           NOT NULL,
    order_id   UUID           NOT NULL,
    sku        VARCHAR(100)   NOT NULL,
    quantity   INTEGER        NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    CONSTRAINT pk_order_lines PRIMARY KEY (id),
    CONSTRAINT fk_order_lines_order FOREIGN KEY (order_id) REFERENCES orders (id)
);