package com.example.oims.fulfillment.infrastructure.persistence;

import com.example.oims.fulfillment.domain.model.Shipment;
import com.example.oims.fulfillment.domain.model.ShipmentStatus;
import com.example.oims.ordering.domain.model.FulfillmentType;

public class ShipmentMapper {

    public static ShipmentJpaEntity toJpa(Shipment shipment) {
        ShipmentJpaEntity entity = new ShipmentJpaEntity(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getFulfillmentType().name(),
                shipment.getCarrierName(),
                shipment.getTrackingCode(),
                shipment.getStatus().name(),
                shipment.getCreatedAt()
        );
        return entity;
    }

    public static Shipment toDomain(ShipmentJpaEntity entity) {
        Shipment shipment = new Shipment(
                entity.getId(),
                entity.getOrderId(),
                FulfillmentType.valueOf(entity.getFulfillmentType()),
                entity.getCarrierName(),
                entity.getTrackingCode(),
                ShipmentStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt()
        );
        return shipment;
    }
}
