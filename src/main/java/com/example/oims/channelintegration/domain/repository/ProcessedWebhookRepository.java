package com.example.oims.channelintegration.domain.repository;

import com.example.oims.channelintegration.domain.model.ProcessedWebhook;

public interface ProcessedWebhookRepository {
    void save(ProcessedWebhook processedWebhook);
    boolean existsByMarketplaceOrderIdAndChannel(String marketplaceOrderId, String channel);
}
