package com.example.oims.inventory.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RestockRequest(
        @NotBlank String sku,
        @Positive int quantity
) {
}
