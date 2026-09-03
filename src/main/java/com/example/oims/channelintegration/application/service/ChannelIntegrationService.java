package com.example.oims.channelintegration.application.service;

import com.example.oims.channelintegration.domain.model.ProcessedWebhook;
import com.example.oims.channelintegration.domain.repository.ProcessedWebhookRepository;
import com.example.oims.ordering.application.service.OrderApplicationService;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.infrastructure.web.dto.OrderLineRequest;
import com.example.oims.shared.infrastructure.redis.RedisService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ChannelIntegrationService {
    private final ProcessedWebhookRepository processedWebhookRepository;
    private final OrderApplicationService orderApplicationService;
    private final RedisService redisService;

    public ChannelIntegrationService(ProcessedWebhookRepository processedWebhookRepository, OrderApplicationService orderApplicationService, RedisService redisService) {
        this.processedWebhookRepository = processedWebhookRepository;
        this.orderApplicationService = orderApplicationService;
        this.redisService = redisService;
    }

    @Transactional
    public Order receiveWebhookOrder(String marketplaceOrderId, String channel, List<OrderLineRequest> lineRequests) {
        String redisKey = "webhook: " + channel + ":" + marketplaceOrderId;
        String cached = redisService.get(redisKey);
        if (cached != null) {
            return orderApplicationService
                    .getOrderByMarketplaceOrderId(marketplaceOrderId);
        }
        // check idempotency
        boolean alreadyProcessed = processedWebhookRepository
                .existsByMarketplaceOrderIdAndChannel(marketplaceOrderId, channel);

        if (alreadyProcessed) {
            return orderApplicationService
                    .getOrderByMarketplaceOrderId(marketplaceOrderId);
        }

        Order order = orderApplicationService.receiveOrder(
                marketplaceOrderId, channel, lineRequests);

        // save idempotency key
        processedWebhookRepository.save(new ProcessedWebhook(marketplaceOrderId, channel));

        redisService.setIfAbsent(
                redisKey,
                "processed",
                Duration.ofHours(1)
        );
        return order;
    }
}
