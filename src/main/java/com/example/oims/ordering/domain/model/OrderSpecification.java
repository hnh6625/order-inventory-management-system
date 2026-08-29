package com.example.oims.ordering.domain.model;

public class OrderSpecification {
    private OrderStatus status;
    private String channel;

    private OrderSpecification() {}

    public static OrderSpecification byStatus(OrderStatus status) {
        OrderSpecification spec = new OrderSpecification();
        spec.status = status;
        return spec;
    }

    public static OrderSpecification byChannel(String channel) {
        OrderSpecification spec = new OrderSpecification();
        spec.channel = channel;
        return spec;
    }

    public OrderSpecification and(OrderSpecification other) {
        OrderSpecification merged = new OrderSpecification();

        merged.status = other.status != null ? other.status : this.status;

        merged.channel = other.channel != null ? other.channel : this.channel;

        return merged;
    }

    public boolean isSatisfiedBy(Order order) {
        return (status == null || order.getStatus() == this.status) &&
                (channel == null || order.getChannel().equals(this.channel));
    }
}
