package com.example.oims.channelintegration.infrastructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TiktokWebhookRequest(
        @NotBlank String order_sn,
        @NotEmpty @Valid List<TiktokOrderItem> item_list
) {}