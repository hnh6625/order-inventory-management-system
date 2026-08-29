package com.example.oims.fulfillment.domain.repository;

import com.example.oims.fulfillment.domain.model.Shipment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {
    void save(Shipment shipment);
    Optional<Shipment> findById(UUID id);
    Optional<Shipment> findByOrderId(UUID orderId);
    List<Shipment> findAll();
}