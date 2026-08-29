package com.example.oims.shared.exception;

import com.example.oims.catalog.domain.model.Size;

public class DuplicateVariantException extends DomainException {
    private final String styleCode;
    private final Size size;
    private final String colorCode;

    public DuplicateVariantException(String styleCode, Size size, String colorCode) {
        super("Variant already exists in style " + styleCode
                + ": size " + size
                + ", color " + colorCode);
        this.styleCode = styleCode;
        this.size = size;
        this.colorCode = colorCode;
    }
}
