package com.example.oims.security.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(
        String token
) {
}
