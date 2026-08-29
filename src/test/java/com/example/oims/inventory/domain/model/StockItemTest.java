package com.example.oims.inventory.domain.model;

import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockItemTest {

    @Test
    void reserve_shouldReduceQuantity() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        StockItem result = stock.reserve(3);

        assertEquals(7,result.getQuantity());
        assertEquals(10,stock.getQuantity());
    }

    @Test
    void reserve_whenQtyExceedsStock_shouldThrow() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),3);

        assertThrows(InsufficientStockException.class, () -> stock.reserve(5));
    }

    @Test
    void reserve_whenQtyNegative_shouldThrow() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        assertThrows(IllegalArgumentException.class, () -> stock.reserve(-1));
    }

    @Test
    void reserve_whenQtyZero_shouldThrow() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"), 10);

        assertThrows(IllegalArgumentException.class,
                () -> stock.reserve(0));
    }

    @Test
    void release_shouldIncreaseQuantity() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        StockItem result = stock.release(3);

        assertEquals(10,stock.getQuantity());
        assertEquals(13,result.getQuantity());
    }

    @Test
    void release_whenQyuZero_shouldThrow() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        assertThrows(IllegalArgumentException.class, () -> stock.release(0));
    }
    @Test
    void restock_shouldIncreaseQuantity() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        StockItem result = stock.restock(3);

        assertEquals(10,stock.getQuantity());
        assertEquals(13,result.getQuantity());
    }

    @Test
    void restock_whenQtyNegative_shouldThrow() {
        StockItem stock = new StockItem(SKU.of("BT001-WHT-M"),10);

        assertThrows(IllegalArgumentException.class, () -> stock.restock(-1));
    }
}
