package com.example.oims.inventory.domain.model;

import com.example.oims.shared.SKU;
import com.example.oims.shared.exception.InsufficientStockException;

import java.util.UUID;

public class StockItem {
    private final UUID id;
    private final SKU sku;
    private final int quantity;

    public StockItem( SKU sku, int quantity) {
        this(UUID.randomUUID(), sku, quantity);
    }
    // constructor reconstruct từ DB
    public StockItem(UUID id, SKU sku, int quantity) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (sku == null) {
            throw new IllegalArgumentException("SKU must not be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative");
        }
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }
    public SKU getSKU() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }


    public StockItem reserve(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Reserve quantity must not be negative");
        }
        if (qty > this.quantity) {
            throw new InsufficientStockException(sku,qty,quantity);
        }
        return new StockItem(id, sku, quantity - qty);
    }

    // release previously stock back to inventory
    public StockItem release(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Release quantity must be greater than 0");
        }

        return new StockItem(id ,sku, quantity + qty);
    }

    // add newly received inventory from supplier
    public StockItem restock(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0");
        }
        return new StockItem(id ,sku, quantity + qty);
    }

    @Override
    public String toString() {
        return "StockItem{id= "+ id + "sku= " + sku + ", quantity=" + quantity + '}';
    }
}
