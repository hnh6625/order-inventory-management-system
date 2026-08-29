package com.example.oims.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SKUTest {
    @Test
    void of_shouldCreateSKWithCorrectValues() {
        SKU sku = SKU.of("BT001-WHT-M");
        assertEquals("BT001-WHT-M", sku.getValue());
    }

    @Test
    void contructor_shouldThrowWhenBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> SKU.of(""));
        assertThrows(IllegalArgumentException.class,
                () -> SKU.of("    "));
    }

    @Test
    void equals_shouldCompareByValue() {
        SKU sku1 = SKU.of("BT001-WHT-M");
        SKU sku2 = SKU.of("BT001-WHT-M");

        assertEquals(sku1, sku2);
    }
}
