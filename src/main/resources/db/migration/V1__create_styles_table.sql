CREATE TABLE styles
(
    id         UUID         NOT NULL,
    style_code VARCHAR(50)  NOT NULL,
    name       VARCHAR(255) NOT NULL,
    category   VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    CONSTRAINT pk_styles PRIMARY KEY (id),
    CONSTRAINT uq_styles_style_code UNIQUE (style_code)
)