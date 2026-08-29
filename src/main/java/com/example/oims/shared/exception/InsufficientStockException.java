package com.example.oims.shared.exception;

import com.example.oims.shared.SKU;

public class InsufficientStockException extends DomainException{
    private final SKU sku;
    private final int requested;
    private final int available;

    public InsufficientStockException(SKU sku, int requested, int available) {
        super("Insufficient stock for SKU " + sku
                + ": requested " + requested
                + ", available " + available);
        this.sku = sku;
        this.requested = requested;
        this.available = available;
    }
}
