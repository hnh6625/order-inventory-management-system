package com.example.oims.shared;

import java.util.Objects;

public class SKU {
    private final String value;

    public SKU(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        this.value = value.toUpperCase().trim();
    }

    public static SKU of(String value) {
        return new SKU(value);
    }
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SKU sku = (SKU) o;
        return value.equals(sku.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }

}
