package com.example.oims.ordering.domain.model;

import com.example.oims.ordering.domain.event.OrderCancelledEvent;
import com.example.oims.ordering.domain.event.OrderReservedEvent;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.event.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDomainEventTest {

    public Order order;
    public List<OrderLine> lines;

    @BeforeEach
    void setup() {
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
    void cancel_shouldPublishOrderCancelledEvent() {
        order.pullDomainEvents();
        order.cancel();
        List<DomainEvent> events = order.pullDomainEvents();
        assertEquals(1,events.size());
        assertInstanceOf(OrderCancelledEvent.class,events.getFirst());
        OrderCancelledEvent event = (OrderCancelledEvent) events.getFirst();
        assertEquals(order.getId(),event.orderId());
    }

    @Test
    void pullDomainEvents_shouldClearEventsAfterPull() {
        order.pullDomainEvents();
        order.cancel();
        List<DomainEvent> events = order.pullDomainEvents();
        assertEquals(1, events.size());

        List<DomainEvent> events1 = order.pullDomainEvents();
        assertEquals(0, events1.size());
    }

    @Test
    void constructor_shouldPublishOrderReservedEvent() {
        List<DomainEvent> events = order.pullDomainEvents();
        assertEquals(1, events.size());
        assertInstanceOf(OrderReservedEvent.class, events.getFirst());
    }
}
