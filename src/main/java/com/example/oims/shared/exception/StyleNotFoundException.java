package com.example.oims.shared.exception;

public class StyleNotFoundException extends DomainException {
    private final String styleCode;

    public StyleNotFoundException(String styleCode) {
        super("Style Not Found" + styleCode);
        this.styleCode = styleCode;
    }

    public String getStyleCode() {
        return styleCode;
    }

}
