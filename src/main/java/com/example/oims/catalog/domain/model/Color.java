package com.example.oims.catalog.domain.model;

import java.util.Objects;

public final class Color {

    private final String code;   // ex: "WHT", "BLK",
    private final String name;   // ex: "White", "Black"

    public Color(String code, String name) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Color code must not be blank");
        }
        if (code.trim().length() != 3) {
            throw new IllegalArgumentException(
                    "Color code must be exactly 3 characters, got: " + code.trim()
            );
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Color name must not be blank");
        }
        this.code = code.trim().toUpperCase();
        this.name = name.trim();
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return code.equals(color.code);  // identity code
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " (" + name + ")";
    }
}