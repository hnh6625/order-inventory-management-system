package com.example.oims.ordering.domain.model;

import com.example.oims.ordering.domain.event.OrderCancelledEvent;
import com.example.oims.ordering.domain.event.OrderReservedEvent;
import com.example.oims.shared.Money;
import com.example.oims.shared.event.DomainEvent;
import com.example.oims.shared.exception.InvalidStateTransitionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private String marketplaceOrderId;
    private String channel;
    private List<OrderLine> orderLines;
    private OrderStatus status;
    private FulfillmentType fulfillmentType;
    private LocalDateTime createdAt;

    public Order(String marketplaceOrderId, String channel, List<OrderLine> orderLines) {
        if (marketplaceOrderId == null || marketplaceOrderId.isBlank()) {
            throw new IllegalArgumentException("marketplaceOrderId must not be blank");
        }
        if (channel == null || channel.isBlank()) {
            throw new IllegalArgumentException("channel must not be blank");
        }
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("orderLines must not be empty");
        }

        this.id = UUID.randomUUID();
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.orderLines = new ArrayList<>(orderLines);
        this.status = OrderStatus.RESERVED;
        this.createdAt = LocalDateTime.now();
        this.fulfillmentType = null;

        this.domainEvents.add(new OrderReservedEvent(
                this.id,
                this.channel,
                List.copyOf(this.orderLines),
                LocalDateTime.now()
        ));

    }

    public Order(UUID id, String marketplaceOrderId, String channel,
                 List<OrderLine> orderLines, OrderStatus status,
                 FulfillmentType fulfillmentType, LocalDateTime createdAt) {
        this.id = id;
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.orderLines = new ArrayList<>(orderLines);
        this.status = status;
        this.fulfillmentType = fulfillmentType;
        this.createdAt = createdAt;
        this.domainEvents = new ArrayList<>();
    }

    private List<DomainEvent> domainEvents = new ArrayList<>();

    public void confirm() {
        if (this.status  != OrderStatus.RESERVED) {
            throw new InvalidStateTransitionException(this.status, "confirm");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void pack(FulfillmentType type) {
        if (this.status  != OrderStatus.CONFIRMED) {
            throw new InvalidStateTransitionException(this.status, "pack");
        }
        this.fulfillmentType = type;
        this.status = OrderStatus.PACKED;
    }

    public void readyForPickup() {
        if (this.status != OrderStatus.PACKED) {
            throw new InvalidStateTransitionException(this.status, "readyForPickup");
        }
        if (this.fulfillmentType == FulfillmentType.SELF_ARRANGED) {
            throw new InvalidStateTransitionException(this.status,
                    "readyForPickup — not applicable for SELF_ARRANGED");
        }
        this.status = OrderStatus.READY_FOR_PICKUP;
    }

    public void ship() {
        if (this.status != OrderStatus.READY_FOR_PICKUP && this.status != OrderStatus.PACKED) {
            throw new InvalidStateTransitionException(this.status, "ship");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new InvalidStateTransitionException(this.status, "deliver");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if (this.status  == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new InvalidStateTransitionException(this.status, "cancel");
        }
        this.status = OrderStatus.CANCELLED;
        this.domainEvents.add(new OrderCancelledEvent(
                this.id,
                List.copyOf(this.orderLines),
                LocalDateTime.now()
        ));
    }

    public Money calculateTotal() {
        return orderLines.stream()
                .map(OrderLine::subtotal)
                .reduce(new Money(BigDecimal.ZERO), Money::add);
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

    public List<OrderLine> getOrderLines() {
        return Collections.unmodifiableList(orderLines);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order{id=" + id
                + ", marketplaceOrderId=" + marketplaceOrderId
                + ", channel=" + channel
                + ", status=" + status
                + ", fulfillmentType=" + fulfillmentType
                + ", total=" + calculateTotal()
                + "}";
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
