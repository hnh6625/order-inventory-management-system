package com.example.oims.ordering.domain.repository;

import com.example.oims.ordering.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save (Order order);
    Optional<Order> findById (UUID id);
    Optional<Order> findByMarketplaceOrderId (String marketplaceOrderId);
    List<Order> findAll ();

}
