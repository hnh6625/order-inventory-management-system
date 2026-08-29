package com.example.oims.ordering.domain.event;

import com.example.oims.ordering.domain.model.OrderLine;
import com.example.oims.shared.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCancelledEvent(
        UUID orderId,
        List<OrderLine> cancelledLines,
        LocalDateTime occurredAt
) implements DomainEvent {}
