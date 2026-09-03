package com.example.oims.ordering.infrastructure.web;

import com.example.oims.ordering.application.service.OrderApplicationService;
import com.example.oims.ordering.domain.model.Order;
import com.example.oims.ordering.infrastructure.web.dto.OrderResponse;
import com.example.oims.ordering.infrastructure.web.dto.PackOrderRequest;
import com.example.oims.ordering.infrastructure.web.dto.ReceiveOrderRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = orderApplicationService.getAllOrders()
                .stream()
                .map(OrderResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        Order order = orderApplicationService.getOrderById(id);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> receiveOrder (@RequestBody @Valid ReceiveOrderRequest request) {
        Order order = orderApplicationService.receiveOrder(
                request.marketplaceOrderId(),
                request.channel(),
                request.lineRequests()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderResponse.from(order));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable UUID id) {
        orderApplicationService.confirmOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/pack")
    public ResponseEntity<Void> packOrder(@PathVariable UUID id,
                                          @RequestBody PackOrderRequest request) {
        orderApplicationService.packOrder(id, request.fulfillmentType());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderApplicationService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<Void> shipOrder(@PathVariable UUID id) {
        orderApplicationService.shipOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<Void> deliverOrder(@PathVariable UUID id) {
        orderApplicationService.deliverOrder(id);
        return ResponseEntity.ok().build();
    }


}
