package com.example.oims.ordering.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "marketplace_order_id", nullable = false, unique = true)
    private String marketplaceOrderId;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = false)
    private String status;

    @Column(name = "fulfillment_type")
    private String fulfillmentType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<OrderLineJpaEntity> orderLines = new ArrayList<>();

    protected OrderJpaEntity() {}

    public OrderJpaEntity(UUID id, String marketplaceOrderId, String channel,
                          String status, String fulfillmentType, LocalDateTime createdAt) {
        this.id = id;
        this.marketplaceOrderId = marketplaceOrderId;
        this.channel = channel;
        this.status = status;
        this.fulfillmentType = fulfillmentType;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getMarketplaceOrderId() { return marketplaceOrderId; }
    public String getChannel() { return channel; }
    public String getStatus() { return status; }
    public String getFulfillmentType() { return fulfillmentType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderLineJpaEntity> getOrderLines() { return orderLines; }
    public void setStatus(String status) { this.status = status; }
    public void setFulfillmentType(String fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

}
