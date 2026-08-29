package com.example.oims.fulfillment.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordShipmentRequest(
        @NotBlank String carrierName,
        String trackingCode
) {
}
