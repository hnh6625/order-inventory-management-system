package com.example.oims.ordering.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderFactoryTest {
    @Test
    void create_shouldReturnOrder_whenValidInput() {
        List<OrderLine> orderLines = List.of(
                new OrderLine(
                        SKU.of("BT001-WHT-M"),
                        2,
                        new Money(new BigDecimal("150000"))
                ),
                new OrderLine(
                        SKU.of("HD002-BLK-L"),
                        1,
                        new Money(new BigDecimal("350000"))
                )
        );
        Order order = OrderFactory.create("SP-001","SHOPEE", orderLines);

        assertEquals(OrderStatus.RESERVED,order.getStatus());
        assertEquals("SHOPEE",order.getChannel());
        assertEquals(2,order.getOrderLines().size());
    }

    @Test
    void create_shouldThrow_whenLineRequestsEmpty() {
        List<OrderLine> orderLines = List.of();

        assertThrows(IllegalArgumentException.class, () -> OrderFactory.create("SP-001", "SHOPEE", orderLines));
    }

    @Test
    void create_shouldConvertSkuAndMoneyCorrectly() {
        List<OrderLine> orderLines = List.of(
                new OrderLine(
                        SKU.of("BT001-WHT-M"),
                        2,
                        new Money(new BigDecimal("150000"))
                )
        );

        Order order = OrderFactory.create("SP-001", "SHOPEE", orderLines);

        OrderLine line = order.getOrderLines().getFirst();

        assertEquals(SKU.of("BT001-WHT-M"),line.getSku());
        assertEquals(2,line.getQuantity());
        assertEquals(new Money(new BigDecimal("150000")),line.getUnitPrice());
    }
}
