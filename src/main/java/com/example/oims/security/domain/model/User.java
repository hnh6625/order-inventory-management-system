package com.example.oims.security.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public User(UUID id, String username, String password, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
