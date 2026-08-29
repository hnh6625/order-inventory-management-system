package com.example.oims.fulfillment.application.service;

import com.example.oims.fulfillment.domain.model.Shipment;
import com.example.oims.fulfillment.domain.repository.ShipmentRepository;
import com.example.oims.ordering.domain.model.FulfillmentType;
import com.example.oims.ordering.domain.repository.OrderRepository;
import com.example.oims.shared.exception.OrderNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FulfillmentApplicationService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;

    public FulfillmentApplicationService(ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Shipment createShipment(UUID orderId, FulfillmentType fulfillmentType) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        Shipment result = new Shipment(orderId, fulfillmentType);
        shipmentRepository.save(result);
        return result;
    }

    @Transactional
    public void recordSelfArrangedShipment(UUID shipmentId, String carrierName, String trackingCode) {
        Shipment shipment = getShipmentByShipmentId(shipmentId);
        shipment.recordSelfArrangedShipment(carrierName, trackingCode);
        shipmentRepository.save(shipment);
    }

    @Transactional
    public void confirmDelivered(UUID shipmentId) {
        Shipment shipment = getShipmentByShipmentId(shipmentId);
        shipment.markAsDelivered();
        shipmentRepository.save(shipment);
    }

    @Transactional
    public void confirmFailed(UUID shipmentId) {
        Shipment shipment = getShipmentByShipmentId(shipmentId);
        shipment.markAsFailed();
        shipmentRepository.save(shipment);
    }

    public Shipment getShipmentByOrderId(UUID orderId) {
        return shipmentRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Shipment getShipmentByShipmentId(UUID shipmentId) {
        return shipmentRepository.findById(shipmentId).orElseThrow(() -> new OrderNotFoundException(shipmentId));
    }
}
