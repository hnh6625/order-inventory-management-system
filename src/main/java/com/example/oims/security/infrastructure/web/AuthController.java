package com.example.oims.security.infrastructure.web;

import com.example.oims.security.application.service.AuthApplicationService;
import com.example.oims.security.infrastructure.web.dto.LoginRequest;
import com.example.oims.security.infrastructure.web.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {
        String token = authApplicationService.login(
                request.username(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
