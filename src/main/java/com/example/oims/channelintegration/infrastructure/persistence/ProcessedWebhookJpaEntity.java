package com.example.oims.channelintegration.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_webhooks")
public class ProcessedWebhookJpaEntity {

    @Id
    private UUID id;

    @Column(name = "marketplace_order_id", nullable = false)
    private String marketplaceOrderId;

    @Column(nullable = false)
    private String channel;

    @Column(name = "processed_at" , nullable = false)
    private LocalDateTime processedAt;

    protected  ProcessedWebhookJpaEntity() {}

    public ProcessedWebhookJpaEntity(UUID id, String marketplaceOrderId, String channel, LocalDateTime processedAt) {
        this.id = id;
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.processedAt = processedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarketplaceOrderId() {
        return marketplaceOrderId;
    }

    public void setMarketplaceOrderId(String marketplaceOrderId) {
        this.marketplaceOrderId = marketplaceOrderId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
