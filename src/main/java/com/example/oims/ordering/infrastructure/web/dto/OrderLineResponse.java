package com.example.oims.ordering.infrastructure.web.dto;

import com.example.oims.ordering.domain.model.OrderLine;

import java.math.BigDecimal;

public record OrderLineResponse (
        String sku,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    public static OrderLineResponse from(OrderLine line) {
        return new OrderLineResponse(
                line.getSku().getValue(),
                line.getQuantity(),
                line.getUnitPrice().getAmount(),
                line.subtotal().getAmount()
        );
    }
}
