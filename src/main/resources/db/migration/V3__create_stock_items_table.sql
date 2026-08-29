CREATE TABLE stock_items
(
    id       UUID         NOT NULL,
    sku      VARCHAR(100) NOT NULL,
    quantity INTEGER      NOT NULL DEFAULT 0,
    version  BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_stock_items PRIMARY KEY (id),
    CONSTRAINT uq_stock_items UNIQUE (sku),
    CONSTRAINT chk_stock_items_quantity CHECK ( quantity > 0 )
);