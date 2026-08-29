package com.example.oims.shared.exception;

import com.example.oims.shared.SKU;

public class DuplicateSkuException extends DomainException {
    private final SKU sku;

    public DuplicateSkuException(SKU sku) {
        super("SKU already exists: " + sku);
        this.sku = sku;
    }
}
