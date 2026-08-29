package com.example.oims.shared;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    @Test
    void contructor_shouldThrowWhenNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(new BigDecimal("-1")));
    }

    @Test
    void contructor_shouldRoundToScale2() {
        Money money = new Money(new BigDecimal("150000.999"));
        assertEquals(new BigDecimal("150001.00"), money.getAmount());
    }
    @Test
    void add_shouldReturnCorrectSum() {
        Money a = new Money(new BigDecimal("100000"));
        Money b = new Money(new BigDecimal("50000"));
        assertEquals(new Money(new BigDecimal("150000")), a.add(b));
    }

    @Test
    void subtract_shouldThrowWhenResultNegative() {
        Money a = new Money(new BigDecimal("50000"));
        Money b = new Money(new BigDecimal("100000"));

        assertThrows(IllegalArgumentException.class,
                () -> a.subtract(b));
    }
}
