package com.example.oims.channelintegration.infrastructure.web;

import com.example.oims.channelintegration.application.service.ChannelIntegrationService;
import com.example.oims.channelintegration.infrastructure.web.dto.ShopeeWebhookRequest;
import com.example.oims.channelintegration.infrastructure.web.dto.TiktokWebhookRequest;
import com.example.oims.channelintegration.infrastructure.web.dto.WebhookResponse;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.infrastructure.web.dto.OrderLineRequest;
import com.example.oims.shared.exception.InsufficientStockException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "apiKeyAuth")
@RestController
@RequestMapping("/webhooks")
public class WebhookController {
    private final ChannelIntegrationService channelIntegrationService;

    public WebhookController(ChannelIntegrationService channelIntegrationService) {
        this.channelIntegrationService = channelIntegrationService;
    }

    // webhook from shopee
    @PostMapping("/shopee/orders")
    public ResponseEntity<WebhookResponse> receiveShopeeOrder(
            @RequestBody ShopeeWebhookRequest request) {
        List<OrderLineRequest> lineRequests = request.items()
                .stream()
                .map(item -> new OrderLineRequest(
                        item.skuId(),
                        item.quantity(),
                        item.price()
                ))
                .toList();
        try {
            Order order = channelIntegrationService.receiveWebhookOrder(
                    request.orderId(),
                    "SHOPEE",
                    lineRequests
            );

            return ResponseEntity.ok(new WebhookResponse(
                            "CONFIRMED",
                            order.getId().toString(),
                            null
                    )
            );
        } catch (InsufficientStockException e) {
            return ResponseEntity.ok(new WebhookResponse(
                    "REJECTED",
                    request.orderId().toString(),
                    "INSUFFICIENT_STOCK"
            ));
        }
    }

    @PostMapping("/tiktokshop/orders")
    public ResponseEntity<WebhookResponse> receiveTiktokOrder(
            @RequestBody TiktokWebhookRequest request) {
        List<OrderLineRequest> lineRequests = request.item_list()
                .stream()
                .map(item -> new OrderLineRequest(
                        item.sku_id(),
                        item.quantity(),
                        item.original_price()
                ))
                .toList();
        try {
            Order order = channelIntegrationService.receiveWebhookOrder(
                    request.order_sn(),
                    "TIKTOK",
                    lineRequests
            );

            return ResponseEntity.ok(new WebhookResponse(
                            "CONFIRMED",
                            order.getId().toString(),
                            null
                    )
            );
        } catch (InsufficientStockException e) {
            return ResponseEntity.ok(new WebhookResponse(
                    "REJECTED",
                    request.order_sn(),
                    "INSUFFICIENT_STOCK"
            ));
        }

    }
}
