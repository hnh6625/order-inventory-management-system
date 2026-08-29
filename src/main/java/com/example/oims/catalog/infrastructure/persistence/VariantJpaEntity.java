package com.example.oims.catalog.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "variants")
public class VariantJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, length = 10)
    private String size;

    @Column(name = "color_code", nullable = false, length = 3)
    private String colorCode;

    @Column(name = "color_name", nullable = false, length = 50)
    private String colorName;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id", nullable = false)
    private StyleJpaEntity style;

    protected VariantJpaEntity() {
    }


    public VariantJpaEntity(UUID id, String sku, String size, String colorCode, String colorName, BigDecimal price, StyleJpaEntity style) {
        this.id = id;
        this.sku = sku;
        this.size = size;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.price = price;
        this.style = style;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public StyleJpaEntity getStyle() {
        return style;
    }

    public void setStyle(StyleJpaEntity style) {
        this.style = style;
    }
}
