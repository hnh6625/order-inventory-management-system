package com.example.oims.catalog.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.DuplicateVariantException;
import com.sun.jdi.request.DuplicateRequestException;

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

    public Style(String styleCode, String name, String category) {
        this(UUID.randomUUID(), styleCode, name, category);
    }

    // recontruct style từ db - nhận uuid có sẵn
    public Style(UUID id, String styleCode, String name, String category) {
        if (styleCode == null || styleCode.isBlank()) {
            throw new IllegalArgumentException("Style code must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Style name must not be blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category must not be blank");
        }
        this.id = id;
        this.styleCode = styleCode.trim().toUpperCase();
        this.name = name.trim();
        this.category = category.trim();
        this.variants = new ArrayList<>();
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

        Variant variant = new Variant(id, sku, size, color, price);
        variants.add(variant);
        return variant;
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

    public String toString() {
        return "Style{code= " +styleCode + ", name= " + name + ", variants= " + variants.size() + '}';
    }
}