package com.example.oims.inventory.infrastructure.persistence;

import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.inventory.domain.repository.StockItemRepository;
import com.example.oims.shared.SKU;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class StockItemRepositoryAdapter implements StockItemRepository {

    private final StockItemJpaRepository stockItemJpaRepository;

    public StockItemRepositoryAdapter(StockItemJpaRepository stockItemJpaRepository) {
        this.stockItemJpaRepository = stockItemJpaRepository;
    }

    @Override
    public void save(StockItem stockItem) {
        StockItemJpaEntity jpaEntity = stockItemJpaRepository
                .findById(stockItem.getId())
                .orElse(null);
        if (jpaEntity != null) {
            jpaEntity.setQuantity(stockItem.getQuantity());
            return;
        }
        stockItemJpaRepository.save(
                StockItemMapper.toJpa(stockItem)
        );
    }

        @Override
    public Optional<StockItem> findBySku(SKU sku) {
        return stockItemJpaRepository.findBySku(sku.getValue()).map(StockItemMapper::toDomain);
    }

    @Override
    public List<StockItem> findAll() {
        return stockItemJpaRepository.findAll().stream().map(StockItemMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        stockItemJpaRepository.deleteById(id);
    }
}
