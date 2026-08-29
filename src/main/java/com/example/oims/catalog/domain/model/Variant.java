package com.example.oims.catalog.domain.model;

import com.example.oims.shared.Money;
import com.example.oims.shared.SKU;

import java.util.Objects;
import java.util.UUID;

public class Variant {
    private final UUID id;
    private final SKU sku;
    private final Size size;
    private final Color color;
    private Money price;

    Variant(UUID id,SKU sku, Size size, Color color, Money price) {
        if (sku == null) throw new IllegalArgumentException("SKU must not be null");
        if (size == null) throw new IllegalArgumentException("Size must not be null");
        if (color == null) throw new IllegalArgumentException("Color must not be null");
        if (price == null) throw new IllegalArgumentException("Price must not be null");
        this.id = UUID.randomUUID();
        this.sku = sku;
        this.size = size;
        this.color = color;
        this.price = price;
    }


    public void updatePrice(Money newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("New price must not be null");
        }
        this.price = newPrice;
    }

    public UUID getId() {return id;}
    public SKU getSKU() { return sku; }
    public Size getSize() { return size; }
    public Color getColor() { return color; }
    public Money getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return sku.equals(variant.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return "Variant{id= " + " sku= " + sku + ", size= " + size + ", color= " + color.getCode() + ", price= " + price + "}";
    }
}
