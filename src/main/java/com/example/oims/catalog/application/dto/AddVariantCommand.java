package com.example.oims.catalog.application.dto;

import com.example.oims.catalog.domain.model.Color;
import com.example.oims.catalog.domain.model.Size;
import com.example.oims.shared.Money;

public record AddVariantCommand(
        String styleCode,
        Size size,
        Color color,
        Money price
) {}
