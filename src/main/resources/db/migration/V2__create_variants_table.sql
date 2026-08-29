CREATE TABLE variants
(
    id         UUID           NOT NULL,
    style_id   UUID           NOT NULL,
    sku        VARCHAR(100)   NOT NULL,
    size       VARCHAR(10)    NOT NULL,
    color_code VARCHAR(3)     NOT NULL,
    color_name VARCHAR(50)    NOT NULL,
    price      DECIMAL(15, 2) NOT NULL,
    CONSTRAINT pk_variants PRIMARY KEY (id),
    CONSTRAINT uq_variants_sku UNIQUE (sku),
    CONSTRAINT fk_variants_style FOREIGN KEY (style_id) REFERENCES styles (id)
);