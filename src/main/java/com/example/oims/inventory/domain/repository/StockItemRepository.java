package com.example.oims.inventory.domain.repository;

import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.shared.SKU;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockItemRepository {
    void save(StockItem stockItem);
    Optional<StockItem> findBySku(SKU sku);
    List<StockItem> findAll();
    void deleteById(UUID id);
}
