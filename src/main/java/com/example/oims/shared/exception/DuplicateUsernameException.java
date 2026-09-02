package com.example.oims.shared.exception;

public class DuplicateUsernameException extends DomainException {
    private final String username;

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
        this.username = username;
    }
}
