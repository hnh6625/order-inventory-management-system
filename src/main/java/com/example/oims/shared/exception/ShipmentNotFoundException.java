package com.example.oims.shared.exception;

import java.util.UUID;

public class ShipmentNotFoundException extends DomainException {
    private final UUID orderId;
    public ShipmentNotFoundException(UUID orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;
    }
}
