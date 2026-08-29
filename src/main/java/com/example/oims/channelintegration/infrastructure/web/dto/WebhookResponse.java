package com.example.oims.channelintegration.infrastructure.web.dto;

public record WebhookResponse(
        String status,    // "CONFIRMED" or "REJECTED"
        String orderId,
        String reason     // null nếu CONFIRMED, "INSUFFICIENT_STOCK" nếu REJECTED
) {}