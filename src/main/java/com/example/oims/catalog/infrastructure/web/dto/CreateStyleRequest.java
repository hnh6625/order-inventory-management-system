package com.example.oims.catalog.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateStyleRequest(
        @NotBlank String styleCode,
        @NotBlank String name,
        @NotBlank String category
) {}