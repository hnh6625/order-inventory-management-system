package com.example.oims.security.domain.repository;

import com.example.oims.security.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserName(String username);
    boolean existsByUsername(String username);
    List<User> findAll();
    void save(User user);
}
