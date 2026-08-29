package com.example.oims.ordering.infrastructure.web.dto;

import com.example.oims.ordering.domain.model.FulfillmentType;
import jakarta.validation.constraints.NotNull;

public record PackOrderRequest(
        @NotNull FulfillmentType fulfillmentType
        ) {}
