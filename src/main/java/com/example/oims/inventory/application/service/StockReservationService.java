package com.example.oims.inventory.application.service;

import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.inventory.domain.repository.StockItemRepository;
import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.StockItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StockReservationService {
    private final StockItemRepository stockItemRepository;

    public StockReservationService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    private StockItem findOrThrow(SKU sku) {
        return stockItemRepository.findBySku(sku)
                .orElseThrow(() -> new StockItemNotFoundException(sku));
    }

    @Transactional
    public void reserve(SKU sku, int quantity) {
        StockItem updated = findOrThrow(sku).reserve(quantity);

        stockItemRepository.save(updated);

    }
    @Transactional
    public void release(SKU sku, int quantity) {
        StockItem updated = findOrThrow(sku).release(quantity);
        stockItemRepository.save(updated);
    }

    @Transactional
    public void restock(SKU sku, int quantity) {
        StockItem updated = stockItemRepository.findBySku(sku)
                .map(existing -> existing.restock(quantity))  // đã có → cộng thêm
                .orElse(new StockItem(sku, quantity));         // chưa có → tạo mới

        stockItemRepository.save(updated);
    }

    public StockItem findBySku(SKU sku) {
        return findOrThrow(sku);
    }

    public List<StockItem> findAll() {
        return stockItemRepository.findAll();
    }
}
