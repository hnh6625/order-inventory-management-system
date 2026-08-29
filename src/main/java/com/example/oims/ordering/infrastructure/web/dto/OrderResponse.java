package com.example.oims.ordering.infrastructure.web.dto;

import com.example.oims.ordering.domain.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String marketplaceOrderId,
        String channel,
        String status,
        String fulfillmentType,
        List<OrderLineResponse> orderLines,
        BigDecimal total
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getMarketplaceOrderId(),
                order.getChannel(),
                order.getStatus().name(),
                order.getFulfillmentType() != null
                        ? order.getFulfillmentType().name() : null,
                order.getOrderLines().stream()
                        .map(OrderLineResponse::from)
                        .toList(),
                order.calculateTotal().getAmount()
        );
    }
}
