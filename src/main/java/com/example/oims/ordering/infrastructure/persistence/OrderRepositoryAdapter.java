package com.example.oims.ordering.infrastructure.persistence;

import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    @Override
    public void save(Order order) {
        OrderJpaEntity entity = OrderMapper.toJpa(order);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(OrderMapper::toDomain);
    }

    @Override
    public Optional<Order> findByMarketplaceOrderId(String marketplaceOrderId) {
        return jpaRepository.findByMarketplaceOrderId(marketplaceOrderId)
                .map(OrderMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(OrderMapper::toDomain)
                .toList();
    }
}
