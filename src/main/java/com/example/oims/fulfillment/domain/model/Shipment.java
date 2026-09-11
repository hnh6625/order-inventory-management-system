package com.example.oims.fulfillment.domain.model;

import com.example.oims.ordering.domain.model.FulfillmentType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Shipment {
    private final UUID id;
    private final UUID orderId;
    private final FulfillmentType fulfillmentType;
    private String carrierName;
    private String trackingCode;
    private ShipmentStatus status;
    private final LocalDateTime createdAt;

    public Shipment(UUID orderId, FulfillmentType fulfillmentType) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.fulfillmentType = fulfillmentType;
        this.status = ShipmentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor reconstruct từ DB
    public Shipment(UUID id, UUID orderId, FulfillmentType fulfillmentType,
                    String carrierName, String trackingCode,
                    ShipmentStatus status, LocalDateTime createdAt) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (orderId == null) throw new IllegalArgumentException("Order id must not be null");
        if (fulfillmentType == null) throw new IllegalArgumentException("Fulfillment type must not be null");
        if (status == null) throw new IllegalArgumentException("Status must not be null");
        if (createdAt == null) throw new IllegalArgumentException("Created at must not be null");
        this.id = id;
        this.orderId = orderId;
        this.fulfillmentType = fulfillmentType;
        this.carrierName = carrierName;
        this.trackingCode = trackingCode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void recordSelfArrangedShipment(String carrierName, String trackingCode) {
        if (this.fulfillmentType != FulfillmentType.SELF_ARRANGED) {
            throw new IllegalStateException(
                    "Cannot record self-arranged shipment for MARKETPLACE_MANAGED order"
            );
        }

        if (this.status != ShipmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Shipment can only be recorded from PENDING state"
            );
        }
        this.carrierName = carrierName;
        this.trackingCode = trackingCode;
        this.status = ShipmentStatus.PICKED_UP;
    }

    public void markAsDelivered() {
        if (this.status != ShipmentStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Shipment can only be delivered from DELIVERED state"
            );
        }
        this.status = ShipmentStatus.DELIVERED;
    }

    public void markAsFailed() {
        if (this.status != ShipmentStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Shipment can only be delivered from DELIVERED state"
            );
        }
        this.status = ShipmentStatus.FAILED;
    }

    public void startDelivering() {
        if (this.status != ShipmentStatus.PICKED_UP) {
            throw new IllegalStateException(
                    "Shipment can only start delivering from PICKED_UP state"
            );
        }

        this.status = ShipmentStatus.DELIVERING;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public FulfillmentType getFulfillmentType() { return fulfillmentType; }
    public String getCarrierName() { return carrierName; }
    public String getTrackingCode() { return trackingCode; }
    public ShipmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}