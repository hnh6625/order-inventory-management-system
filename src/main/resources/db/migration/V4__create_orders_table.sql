CREATE TABLE orders
(
    id                   UUID         NOT NULL,
    marketplace_order_id VARCHAR(100) NOT NULL,
    channel              VARCHAR(50)  NOT NULL,
    status               VARCHAR(50)  NOT NULL,
    fulfillment_type     VARCHAR(50),
    created_at           TIMESTAMP    NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT uq_orders_marketplace_order_id UNIQUE (marketplace_order_id)
);