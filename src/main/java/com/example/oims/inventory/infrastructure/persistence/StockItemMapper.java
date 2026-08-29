package com.example.oims.inventory.infrastructure.persistence;

import com.example.oims.inventory.domain.model.StockItem;
import com.example.oims.shared.SKU;

public class StockItemMapper {

    public static StockItemJpaEntity toJpa(StockItem stockItem) {
        StockItemJpaEntity stockItemJpaEntity = new StockItemJpaEntity(
                stockItem.getId(),
                stockItem.getSKU().getValue(),
                stockItem.getQuantity()
        );
        return stockItemJpaEntity;
    }

    public static StockItem toDomain(StockItemJpaEntity stockItemJpaEntity) {
        StockItem stockItem = new StockItem(
                stockItemJpaEntity.getId(),
                SKU.of(stockItemJpaEntity.getSku()),
                stockItemJpaEntity.getQuantity()
        );
        return stockItem;
    }
}
