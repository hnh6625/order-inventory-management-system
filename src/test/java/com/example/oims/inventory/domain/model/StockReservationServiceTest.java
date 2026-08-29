package com.example.oims.inventory.domain.model;

import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.inventory.domain.repository.StockItemRepository;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.InsufficientStockException;
import com.example.oims.shared.exception.StockItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockReservationServiceTest {
    private StockItemRepository stockItemRepository;
    private StockReservationService service;

    @BeforeEach
    void setUp() {
        stockItemRepository = Mockito.mock(StockItemRepository.class);
        service = new StockReservationService(stockItemRepository);
    }

    @Test
    void reserve_shouldReduceStock_whenSufficientStock() {
        SKU sku = SKU.of("BT001-WHT-M");
        StockItem stockItem = new StockItem(sku, 10);

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.of(stockItem));

        service.reserve(sku, 3);

        Mockito.verify(stockItemRepository)
                .save(Mockito.argThat(item -> item.getQuantity() == 7));
    }

    @Test
    void reserve_shouldThrow_whenInsufficientStock() {
        SKU sku = SKU.of("BT001-WHT-M");
        StockItem stockItem = new StockItem(sku, 3);

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.of(stockItem));

        assertThrows(InsufficientStockException.class,() -> service.reserve(sku, 5));
    }

    @Test
    void reserve_shouldThrow_whenSkuNotFound() {
        SKU sku = SKU.of("UNKNOWN-SKU");

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class,
                () -> service.reserve(sku, 1));
    }

    @Test
    void reserve_shouldNotSave_whenReserveFails() {
        SKU sku = SKU.of("BT001-WHT-M");
        StockItem stockItem = new StockItem(sku, 2);

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.of(stockItem));

        assertThrows(InsufficientStockException.class,
                () -> service.reserve(sku, 5));

        Mockito.verify(stockItemRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void release_shouldIncreaseStock_whenCancelled() {
        SKU sku = SKU.of("BT001-WHT-M");
        StockItem stockItem = new StockItem(sku, 7);

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.of(stockItem));

        service.release(sku,3);

        Mockito.verify(stockItemRepository)
                .save(Mockito.argThat(item -> item.getQuantity() == 10));
    }

    @Test
    void restock_shouldIncreaseStock_whenWarehouseRestocks() {
        SKU sku = SKU.of("BT001-WHT-M");
        StockItem stockItem = new StockItem(sku, 5);

        Mockito.when(stockItemRepository.findBySku(sku))
                .thenReturn(Optional.of(stockItem));

        service.restock(sku,20);

        Mockito.verify(stockItemRepository)
                .save(Mockito.argThat(item -> item.getQuantity() == 25));
    }
}
