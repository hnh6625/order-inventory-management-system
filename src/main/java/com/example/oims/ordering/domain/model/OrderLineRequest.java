package com.example.oims.ordering.domain.model;

import java.math.BigDecimal;

public record OrderLineRequest (
    String sku,
    int quantity,
    BigDecimal unitPrice
) {}
