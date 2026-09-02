package com.example.oims.security.application.service;

import com.example.oims.security.domain.model.User;
import com.example.oims.security.domain.model.UserRole;
import com.example.oims.security.domain.repository.UserRepository;
import com.example.oims.shared.exception.DuplicateUsernameException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserApplicationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(String username, String password, UserRole role) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }

        User user = new User(
                UUID.randomUUID(),
                username,
                passwordEncoder.encode(password),
                role,
                LocalDateTime.now()
        );

        userRepository.save(user);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
