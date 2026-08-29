package com.example.oims.ordering.infrastructure.web.dto;

import com.example.oims.ordering.domain.model.OrderLineRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReceiveOrderRequest(
        @NotBlank String marketplaceOrderId,
        @NotBlank String channel,
        @NotEmpty List<OrderLineRequest> lineRequests
) {
}
