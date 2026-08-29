package com.example.oims.ordering.domain.model;

import com.example.oims.ordering.domain.model.FulfillmentType;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.ordering.domain.model.OrderStatus;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    private List<OrderLine> lines;
    private Order order;

    @BeforeEach
    public void setUp() {
        lines = List.of(
                new OrderLine(SKU.of("BT001-WHT-M"), 2, new Money(new BigDecimal("150000"))),
                new OrderLine(SKU.of("HD002-BLK-L"), 1, new Money(new BigDecimal("350000")))
        );
        order = new Order("SP-001", "SHOPEE", lines);
    }

    @Test
    void confirm_shouldChangeStatus_whenReserved() {
        OrderStatus status = OrderStatus.RESERVED;
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void confirm_shouldThrow_whenNotReserved() {
        assertThrows(InvalidStateTransitionException.class, () -> {
            order.confirm();
            order.confirm();
        });
    }

    @Test
    void cancel_shouldChangeStatus_whenReserved() {
        OrderStatus status = OrderStatus.RESERVED;
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancel_shouldThrow_whenShipped() {
        assertThrows(InvalidStateTransitionException.class, () -> {
            order.confirm();
            order.pack(FulfillmentType.SELF_ARRANGED);
            order.ship();
            order.cancel();
        });
    }

    @Test
    void calculateTotal_shouldReturnSumOfAllLines() {
        assertEquals(new Money(new BigDecimal("650000")),order.calculateTotal());
    }
}
