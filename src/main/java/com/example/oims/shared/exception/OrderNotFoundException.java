package com.example.oims.shared.exception;

import java.util.UUID;

public class OrderNotFoundException extends DomainException {
    private final UUID orderId;
    private final String marketplaceOrderId;

    public OrderNotFoundException(UUID orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;
        this.marketplaceOrderId = null;
    }

    public OrderNotFoundException(String marketplaceOrderId) {
        super("Order not found: marketplaceOrderId=" + marketplaceOrderId);
        this.orderId = null;
        this.marketplaceOrderId = marketplaceOrderId;
    }
}
