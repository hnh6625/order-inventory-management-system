package com.example.oims.fulfillment.infrastructure.web.dto;

import com.example.oims.fulfillment.domain.model.Shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentResponse(
        UUID id,
        UUID orderId,
        String fulfillmentType,
        String carrierName,
        String trackingCode,
        String status,
        LocalDateTime createdAt
) {
    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getFulfillmentType().name(),
                shipment.getCarrierName(),
                shipment.getTrackingCode(),
                shipment.getStatus().name(),
                shipment.getCreatedAt()
        );
    }
}
