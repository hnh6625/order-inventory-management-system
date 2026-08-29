package com.example.oims.catalog.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VariantResponse(
        UUID id,
        String sku,
        String size,
        String colorCode,
        String colorName,
        BigDecimal price
) {
}