package com.example.oims.channelintegration.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TiktokWebhookRequest(
        @NotBlank String order_sn,
        @NotEmpty List<TiktokOrderItem> item_list
) {}