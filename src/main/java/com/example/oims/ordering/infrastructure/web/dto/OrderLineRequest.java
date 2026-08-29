package com.example.oims.ordering.infrastructure.web.dto;

import java.math.BigDecimal;

public record OrderLineRequest (
    String sku,
    int quantity,
    BigDecimal unitPrice
) {}
