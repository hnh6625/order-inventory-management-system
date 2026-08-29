package com.example.oims.integration;

import com.example.oims.inventory.application.service.StockReservationService;
import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.inventory.domain.repository.StockItemRepository;
import com.example.oims.shared.SKU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ConcurrentReserveIntegrationTest {

    @Autowired
    private StockReservationService stockReservationService;

    @Autowired
    private StockItemRepository stockItemRepository;

    private final SKU sku = SKU.of("BT001-WHT-M");

    @BeforeEach
    void Setup() {
        stockItemRepository.findBySku(sku)
                .ifPresent(item -> stockItemRepository.deleteById(item.getId()));

        stockReservationService.restock(sku,10);
    }

    @Test
    void reserve_shouldNotOversell_whenConcurrentRequests() throws InterruptedException {
        int threadCount = 20;
        int reserveQty = 1;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            tasks.add(() -> {
                try {
                    latch.await();
                    stockReservationService.reserve(sku,reserveQty);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            });
        }

        tasks.forEach(executor::submit);
        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(30,java.util.concurrent.TimeUnit.SECONDS);

        StockItem finalStock = stockItemRepository.findBySku(sku).orElseThrow();
        System.out.println("Success: " + successCount.get());
        System.out.println("Failed:  " + failCount.get());
        System.out.println("Final stock: " + finalStock.getQuantity());

        // Stock không bao giờ âm
        assertTrue(finalStock.getQuantity() >= 0,
                "Stock must not be negative");

        // Tổng success + remaining stock = 10 (ban đầu)
        assertEquals(10,
                successCount.get() + finalStock.getQuantity(),
                "Total reserved + remaining must equal initial stock");
    }
}
