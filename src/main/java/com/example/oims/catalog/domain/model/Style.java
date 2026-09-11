package com.example.oims.catalog.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.DuplicateVariantException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Style {

    private final UUID id;

    // ví dụ: "BASIC-TEE"
    private final String styleCode;

    // ví dụ: "Áo thun Basic MOC"
    private String name;
    private String category;
    private final List<Variant> variants;
    private LocalDateTime createdAt;

    public Style(String styleCode, String name, String category) {
        this(UUID.randomUUID(), styleCode, name, category, LocalDateTime.now());
    }

    // recontruct style từ db - nhận uuid có sẵn
    public Style(UUID id, String styleCode, String name, String category, LocalDateTime createdAt) {
        if (styleCode == null || styleCode.isBlank()) {
            throw new IllegalArgumentException("Style code must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Style name must not be blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category must not be blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at must not be null");
        }
        this.id = id;
        this.styleCode = styleCode.trim().toUpperCase();
        this.name = name.trim();
        this.category = category.trim();
        this.variants = new ArrayList<>();
        this.createdAt = createdAt;
    }


    // Br method
    public Variant addVariant(Size size, Color color, Money price) {
        // // A style cannot contain duplicate size and color combinations.
        boolean isDuplicate = variants.stream()
                .anyMatch(v -> v.getSize() == size
                        && v.getColor().getCode().equals(color.getCode()));

        if (isDuplicate) {
            throw new DuplicateVariantException(styleCode,size,color.getCode());
        }

        // SKU = styleCode + colorCode + size
        // Ex: BT001 + WHT + M => "BT001-WHT-M"
        SKU sku = SKU.of(styleCode + "-" + color.getCode() + "-" + size.name());

        Variant variant = new Variant(UUID.randomUUID(), sku, size, color, price);
        variants.add(variant);
        return variant;
    }

    public void restoreVariant(UUID variantId, Size size, Color color, Money price) {
        SKU sku = SKU.of(styleCode + "-" + color.getCode() + "-" + size.name());

        Variant variant = new Variant(variantId, sku, size, color, price);
        variants.add(variant);
    }


    public UUID getId() {
        return id;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<Variant> getVariants() {
        return Collections.unmodifiableList(variants);
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public String toString() {
        return "Style{code= " +styleCode + ", name= " + name + ", variants= " + variants.size() + '}';
    }
}