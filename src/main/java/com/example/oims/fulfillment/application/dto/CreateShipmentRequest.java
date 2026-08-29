package com.example.oims.fulfillment.application.dto;

import com.example.oims.ordering.domain.model.FulfillmentType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateShipmentRequest(
        @NotNull UUID orderId,
        @NotNull FulfillmentType fulfillmentType
) {
}
