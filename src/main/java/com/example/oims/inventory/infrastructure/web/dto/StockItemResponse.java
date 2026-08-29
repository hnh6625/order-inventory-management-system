package com.example.oims.inventory.infrastructure.web.dto;

import com.example.oims.inventory.domain.model.StockItem;

public record StockItemResponse(
        String sku,
        int quantity
) {
    public static StockItemResponse from(StockItem stockItem) {
        return new StockItemResponse(
                stockItem.getSKU().getValue(),
                stockItem.getQuantity()
        );
    }
}
