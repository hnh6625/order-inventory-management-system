package com.example.oims.ordering.application.service;

import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.ordering.domain.model.FulfillmentType;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderFactory;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.ordering.infrastructure.web.dto.OrderLineRequest;
import com.example.oims.ordering.domain.repository.OrderRepository;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.OrderNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final StockReservationService stockReservationService;

    public OrderApplicationService(OrderRepository orderRepository, StockReservationService stockReservationService) {
        this.orderRepository = orderRepository;
        this.stockReservationService = stockReservationService;
    }

    @Transactional
    public Order receiveOrder(String marketplaceOrderId,
                              String channel,
                              List<OrderLineRequest> lineRequests) {
        Optional<Order> existingOrder = orderRepository.findByMarketplaceOrderId(marketplaceOrderId);
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }

        for (OrderLineRequest lineRequest : lineRequests) {
            stockReservationService.reserve(SKU.of(lineRequest.sku()), lineRequest.quantity());
        }
        List<OrderLine> orderLines = lineRequests.stream()
                .map(request -> new OrderLine(
                        SKU.of(request.sku()),
                        request.quantity(),
                        new Money(request.unitPrice())
                ))
                .toList();
        Order order = OrderFactory.create(marketplaceOrderId, channel, orderLines);
        orderRepository.save(order);
        return order;
    }

    @Transactional
    public void confirmOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.confirm();
        orderRepository.save(order);
    }

    @Transactional
    public void packOrder(UUID orderId, FulfillmentType fulfillmentType) {
        Order order = getOrderById(orderId);
        order.pack(fulfillmentType);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.cancel();
        for (OrderLine line : order.getOrderLines()) {
            stockReservationService.release(
                    line.getSku(),
                    line.getQuantity()
            );
        }
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.ship();
        orderRepository.save(order);
    }

    @Transactional
    public void deliverOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.deliver();
        orderRepository.save(order);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderByMarketplaceOrderId(String marketplaceOrderId) {
        return orderRepository.findByMarketplaceOrderId(marketplaceOrderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with marketplaceOrderId: " + marketplaceOrderId));
    }
}
