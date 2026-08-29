package com.example.oims.channelintegration.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TiktokOrderItem(
        @NotBlank String sku_id,
        @Positive int quantity,
        @NotNull BigDecimal original_price
) {}
