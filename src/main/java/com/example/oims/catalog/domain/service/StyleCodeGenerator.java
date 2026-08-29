package com.example.oims.catalog.domain.service;


public class StyleCodeGenerator {
    public String generate(String category, int sequence) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category is null or empty");

        }
        if (sequence <= 0) {
            throw new IllegalArgumentException("sequence must be greater than zero");
        }

        String normalized = category
                .replace(" ", "")
                .replace("-", "")
                .toUpperCase();

        if (normalized.length() < 2) {
            throw new IllegalArgumentException("Category must contain at least 2 letters.");
        }

        String prefix = normalized.substring(0, 2);
        return prefix + String.format("%03d", sequence);
    }
}
