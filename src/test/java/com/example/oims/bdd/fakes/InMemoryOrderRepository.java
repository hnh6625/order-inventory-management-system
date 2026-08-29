package com.example.oims.bdd.fakes;

import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.repository.OrderRepository;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<UUID, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Order> findByMarketplaceOrderId(String marketplaceOrderId) {
        return store.values().stream()
                .filter(o -> o.getMarketplaceOrderId().equals(marketplaceOrderId))
                .findFirst();
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }
}
