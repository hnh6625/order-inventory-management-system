CREATE TABLE processed_webhooks
(
    id                   UUID         NOT NULL,
    marketplace_order_id VARCHAR(100) NOT NULL,
    channel              VARCHAR(50)  NOT NULL,
    processed_at         TIMESTAMP    NOT NULL,
    CONSTRAINT pk_processed_webhooks PRIMARY KEY (id),
    CONSTRAINT uq_processed_webhooks UNIQUE (marketplace_order_id, channel)
);