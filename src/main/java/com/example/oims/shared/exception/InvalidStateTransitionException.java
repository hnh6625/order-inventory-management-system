package com.example.oims.shared.exception;

import com.example.oims.ordering.domain.model.OrderStatus;

public class InvalidStateTransitionException extends DomainException {
    private final OrderStatus currentStatus;
    private final String attemptedAction;

    public InvalidStateTransitionException(OrderStatus currentStatus, String attemptedAction) {
        super("Cannot " + attemptedAction + " order in status " + currentStatus );
        this.currentStatus = currentStatus;
        this.attemptedAction = attemptedAction;
    }
}
