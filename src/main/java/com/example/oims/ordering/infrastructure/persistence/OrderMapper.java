package com.example.oims.ordering.infrastructure.persistence;

import com.example.oims.ordering.domain.model.FulfillmentType;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.ordering.domain.model.OrderStatus;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;

import java.util.List;

public class OrderMapper {
    public static OrderJpaEntity toJpa(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity(
                order.getId(),
                order.getMarketplaceOrderId(),
                order.getChannel(),
                order.getStatus().name(),
                order.getFulfillmentType() != null ? order.getFulfillmentType().name() : null,
                order.getCreatedAt()
        );

        order.getOrderLines().stream()
                .map(line -> new OrderLineJpaEntity(
                        line.getId(),
                        entity,
                        line.getSku().getValue(),
                        line.getQuantity(),
                        line.getUnitPrice().getAmount()
                ))
                .forEach(entity.getOrderLines()::add);

        return entity;
    }

    public static Order toDomain(OrderJpaEntity entity) {
        List<OrderLine> orderLines = entity.getOrderLines()
                .stream()
                .map(line -> new OrderLine(
                        line.getId(),
                        SKU.of(line.getSku()),
                        line.getQuantity(),
                        new Money(line.getUnitPrice())
                ))
                .toList();
        Order order = new Order(
                entity.getId(),
                entity.getMarketplaceOrderId(),
                entity.getChannel(),
                orderLines,
                OrderStatus.valueOf(entity.getStatus()),
                entity.getFulfillmentType() != null
                        ? FulfillmentType.valueOf(entity.getFulfillmentType())
                        : null,
                entity.getCreatedAt()
        );

        return order;
    }
}
