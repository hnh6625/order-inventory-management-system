CREATE TABLE shipments
(
    id               UUID        NOT NULL,
    order_id         UUID        NOT NULL,
    fulfillment_type VARCHAR(50) NOT NULL,
    carrier_name     VARCHAR(100),
    tracking_code    VARCHAR(100),
    status           VARCHAR(50) NOT NULL,
    created_at       TIMESTAMP   NOT NULL,
    CONSTRAINT pk_shipments PRIMARY KEY (id),
    CONSTRAINT fk_shipments_order FOREIGN KEY (order_id) REFERENCES orders (id)
);