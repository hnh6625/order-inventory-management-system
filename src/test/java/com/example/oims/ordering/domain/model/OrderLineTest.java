package com.example.oims.ordering.domain.model;

import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderLineTest {
    @Test
    void subtotal_shouldReturnCorrectAmount() {
        OrderLine orderLine = new OrderLine(SKU.of("BT001-WHT-M"), 10, new Money(new BigDecimal("300000")));
        Money result = orderLine.subtotal();

        assertEquals(new Money(new BigDecimal("3000000")), result);

    }

    @Test
    void constructor_shouldThrow_whenQuantityZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderLine(new SKU("BT001-WHT-M"),
                        0,
                        new Money(new BigDecimal("300000"))
                )
        );
    }

    @Test
    void equals_shouldCompareBySkuOnly() {
        OrderLine orderLine = new OrderLine(new SKU("BT001-WHT-M"), 10, new Money(new BigDecimal("300000")));
        OrderLine orderLine1 = new OrderLine(new SKU("BT001-WHT-M"), 5, new Money(new BigDecimal("100000")));
        assertEquals(orderLine, orderLine1);
    }
}
