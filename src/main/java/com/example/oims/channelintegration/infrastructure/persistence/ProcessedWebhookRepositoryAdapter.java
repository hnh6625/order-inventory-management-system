package com.example.oims.channelintegration.infrastructure.persistence;

import com.example.oims.channelintegration.domain.model.ProcessedWebhook;
import com.example.oims.channelintegration.domain.repository.ProcessedWebhookRepository;
import org.springframework.stereotype.Component;

@Component
public class ProcessedWebhookRepositoryAdapter implements ProcessedWebhookRepository {

    private ProcessedWebhookJpaRepository jpaRepository;

    public ProcessedWebhookRepositoryAdapter(ProcessedWebhookJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    @Override
    public void save(ProcessedWebhook processedWebhook) {
        ProcessedWebhookJpaEntity entity = new ProcessedWebhookJpaEntity(
                processedWebhook.getId(),
                processedWebhook.getMarketplaceOrderId(),
                processedWebhook.getChannel(),
                processedWebhook.getProcessedAt()
        );
        jpaRepository.save(entity);
    }

    @Override
    public boolean existsByMarketplaceOrderIdAndChannel(String marketplaceOrderId, String channel) {
        return jpaRepository.existsByMarketplaceOrderIdAndChannel(marketplaceOrderId, channel);
    }
}
