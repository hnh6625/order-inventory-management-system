package com.example.oims.catalog.infrastructure.web.dto;

import java.util.List;
import java.util.UUID;

public record StyleResponse(
        UUID id,
        String styleCode,
        String name,
        String category,
        List<VariantResponse> variants
) {}