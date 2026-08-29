package com.example.oims.ordering.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;

import java.util.Objects;
import java.util.UUID;

public class OrderLine {
    private final UUID id;
    private final SKU sku;
    private final int quantity;
    private final Money unitPrice;

    public OrderLine( SKU sku, int quantity, Money unitPrice) {
        this(UUID.randomUUID(), sku, quantity, unitPrice);
    }
    public OrderLine(UUID id ,SKU sku, int quantity, Money unitPrice) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (sku == null) {
            throw new IllegalArgumentException("sku cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity cannot be <= 0");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice cannot be null");
        }
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return sku.equals(orderLine.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return "OrderLine{sku=" + sku + ", quantity=" + quantity + ", unitPrice=" + unitPrice + '}';
    }

    public UUID getId() {return id;}
    public SKU getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public Money getUnitPrice() { return unitPrice; }

}
