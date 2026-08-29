package com.example.oims.bdd.fakes;

import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.inventory.domain.repository.StockItemRepository;
import com.example.oims.shared.SKU;

import java.util.*;

public class InMemoryStockItemRepository implements StockItemRepository {
    private final Map<SKU, StockItem> store = new HashMap<>();


    @Override
    public void save(StockItem stockItem) {
        store.put(stockItem.getSKU(), stockItem);
    }

    @Override
    public Optional<StockItem> findBySku(SKU sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public List<StockItem> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID id) {
        store.entrySet().removeIf(entry -> entry.getValue().getId().equals(id));
    }
}
