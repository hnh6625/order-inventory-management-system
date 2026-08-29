package com.example.oims.channelintegration.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProcessedWebhook {
    private final UUID id;
    private final String marketplaceOrderId;
    private final String channel;
    private final LocalDateTime processedAt;

    public ProcessedWebhook(String marketplaceOrderId, String channel) {
        this.id = UUID.randomUUID();
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.processedAt = LocalDateTime.now();
    }

    public ProcessedWebhook(UUID id, String marketplaceOrderId, String channel, LocalDateTime processedAt) {
        this.id = id;
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.processedAt = processedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getMarketplaceOrderId() {
        return marketplaceOrderId;
    }

    public String getChannel() {
        return channel;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
