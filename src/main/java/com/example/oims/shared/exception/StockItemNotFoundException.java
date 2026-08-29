package com.example.oims.shared.exception;

import com.example.oims.shared.SKU;

public class StockItemNotFoundException extends DomainException {
    private final SKU sku;

    public StockItemNotFoundException(SKU sku) {
        super("Stock item not found for SKU: " + sku);
        this.sku = sku;
    }
}
