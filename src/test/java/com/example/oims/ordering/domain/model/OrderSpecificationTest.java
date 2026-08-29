package com.example.oims.ordering.domain.model;

import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.ordering.domain.model.OrderSpecification;
import com.example.oims.ordering.domain.model.OrderStatus;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderSpecificationTest {
    private Order order;
    private List<OrderLine> lines;
    @BeforeEach
    void setUp() {
        lines = List.of(
                new OrderLine(
                        SKU.of("BT001-WHT-M"),
                        2,
                        new Money(new BigDecimal("150000"))
                )
        );

        order = new Order(
                "SP-001",
                "SHOPEE",
                lines
        );
    }
    @Test
    void byStatus_shouldMatchOrderWithSameStatus() {
        OrderSpecification spec = OrderSpecification.byStatus(OrderStatus.RESERVED);

        boolean result = spec.isSatisfiedBy(order);
        assertTrue(result);
    }

    @Test
    void byStatus_shouldNotMatchOrderWithDifferentStatus() {
        OrderSpecification spec = OrderSpecification.byStatus(OrderStatus.CONFIRMED);

        boolean result = spec.isSatisfiedBy(order);
        assertFalse(result);
    }

    @Test
    void byChannel_shouldMatchOrderWithSameChannel() {
        OrderSpecification spec = OrderSpecification.byChannel(order.getChannel());

        boolean result = spec.isSatisfiedBy(order);
        assertTrue(result);
    }

    @Test
    void and_shouldCombineSpecs() {
        OrderSpecification spec = OrderSpecification.byStatus(OrderStatus.RESERVED)
                .and(OrderSpecification.byChannel(order.getChannel())
                );

        boolean result = spec.isSatisfiedBy(order);
        assertTrue(result);

        OrderSpecification spec1 = OrderSpecification.byStatus(OrderStatus.CONFIRMED)
                .and(OrderSpecification.byChannel(order.getChannel())
                );
        boolean result1 = spec1.isSatisfiedBy(order);
        assertFalse(result1);
    }
}
