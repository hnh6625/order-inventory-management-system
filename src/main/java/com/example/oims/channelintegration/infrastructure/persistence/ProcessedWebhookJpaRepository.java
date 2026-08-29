package com.example.oims.channelintegration.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessedWebhookJpaRepository extends JpaRepository<ProcessedWebhookJpaEntity, UUID> {
    boolean existsByMarketplaceOrderIdAndChannel(String marketplaceOrderId, String channel);
}
