package com.example.oims.channelintegration.application.service;

import com.example.oims.channelintegration.domain.model.ProcessedWebhook;
import com.example.oims.channelintegration.domain.repository.ProcessedWebhookRepository;
import com.example.oims.ordering.application.service.OrderApplicationService;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.domain.model.OrderLineRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelIntegrationService {
    private final ProcessedWebhookRepository processedWebhookRepository;
    private final OrderApplicationService orderApplicationService;

    public ChannelIntegrationService(ProcessedWebhookRepository processedWebhookRepository, OrderApplicationService orderApplicationService) {
        this.processedWebhookRepository = processedWebhookRepository;
        this.orderApplicationService = orderApplicationService;
    }

    @Transactional
    public Order receiveWebhookOrder(String marketplaceOrderId, String channel, List<OrderLineRequest> lineRequests) {
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

        return order;
    }
}
