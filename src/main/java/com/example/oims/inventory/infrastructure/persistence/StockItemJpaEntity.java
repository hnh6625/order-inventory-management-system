package com.example.oims.inventory.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "stock_items")
public class StockItemJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Version
    private Long version;

    protected StockItemJpaEntity() {}

    public StockItemJpaEntity(UUID id, String sku, int quantity) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
    }

    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public Long getVersion() { return version; }
    // JPA cần setter để update
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
