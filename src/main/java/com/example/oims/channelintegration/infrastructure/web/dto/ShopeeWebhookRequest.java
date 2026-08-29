package com.example.oims.channelintegration.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ShopeeWebhookRequest(
        @NotBlank String orderId,
        @NotEmpty List<ShopeeOrderItem> items) {
}
