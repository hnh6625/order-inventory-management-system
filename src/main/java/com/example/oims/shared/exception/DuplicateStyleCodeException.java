package com.example.oims.shared.exception;

public class DuplicateStyleCodeException extends DomainException {
    private final String styleCode;

    public DuplicateStyleCodeException(String styleCode) {
        super("Style code already exists: " + styleCode);
        this.styleCode = styleCode;
    }

    public String getStyleCode() {
        return styleCode;
    }
}
