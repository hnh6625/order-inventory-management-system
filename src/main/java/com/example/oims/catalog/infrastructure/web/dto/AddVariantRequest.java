package com.example.oims.catalog.infrastructure.web.dto;

import com.example.oims.catalog.domain.model.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddVariantRequest(
        @NotNull Size size,
        @NotBlank String colorCode,
        @NotBlank String colorName,
        @NotNull BigDecimal price
) {}