package com.example.oims.security.infrastructure.web;

import com.example.oims.security.application.service.UserApplicationService;
import com.example.oims.security.domain.model.User;
import com.example.oims.security.domain.model.UserRole;
import com.example.oims.security.infrastructure.web.dto.CreateUserRequest;
import com.example.oims.security.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        userApplicationService.createUser(
                request.username(),
                request.password(),
                UserRole.valueOf(request.role())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users =
                userApplicationService.getAllUsers()
                        .stream()
                        .map(UserResponse::from)
                        .toList();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
