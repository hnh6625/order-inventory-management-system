package com.example.oims.ordering.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;

import java.util.List;

public class OrderFactory {
    public static Order create(String marketplaceOrderId,
                               String channel,
                               List<OrderLineRequest> lineRequest) {
        if (marketplaceOrderId == null || marketplaceOrderId.isBlank()) {
            throw new IllegalArgumentException("marketplaceOrderId must not be blank");
        }
        if (channel == null || channel.isBlank()) {
            throw new IllegalArgumentException("channel must not be blank");
        }
        if (lineRequest == null || lineRequest.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one line");
        }

        List<OrderLine> orderLines = lineRequest.stream()
                .map(request -> new OrderLine(
                        SKU.of(request.sku()),
                        request.quantity(),
                        new Money(request.unitPrice())
                ))
                .toList();

        return new Order(
                marketplaceOrderId,
                channel,
                orderLines
        );
    }
}
