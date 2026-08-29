package com.example.oims.fulfillment.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordShipmentRequest(
        @NotBlank String carrierName,
        String trackingCode
) {
}
