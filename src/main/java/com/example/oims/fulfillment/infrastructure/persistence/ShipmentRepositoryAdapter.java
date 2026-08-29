package com.example.oims.fulfillment.infrastructure.persistence;

import com.example.oims.fulfillment.domain.model.Shipment;
import com.example.oims.fulfillment.domain.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ShipmentRepositoryAdapter implements ShipmentRepository {
    private final ShipmentJpaRepository shipmentJpaRepository;

    public ShipmentRepositoryAdapter(@Autowired ShipmentJpaRepository shipmentJpaRepository) {
        this.shipmentJpaRepository = shipmentJpaRepository;
    }

    @Override
    public void save(Shipment shipment) {
        ShipmentJpaEntity shipmentJpaEntity = ShipmentMapper.toJpa(shipment);
        shipmentJpaRepository.save(shipmentJpaEntity);
    }

    @Override
    public Optional<Shipment> findById(UUID id) {
        return shipmentJpaRepository.findById(id)
                .map(ShipmentMapper::toDomain);
    }

    @Override
    public Optional<Shipment> findByOrderId(UUID orderId) {
        return shipmentJpaRepository.findByOrderId(orderId)
                .map(ShipmentMapper::toDomain);
    }

    @Override
    public List<Shipment> findAll() {
        return shipmentJpaRepository.findAll().stream()
                .map(ShipmentMapper::toDomain)
                .toList();
    }
}
