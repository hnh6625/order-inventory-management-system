package com.example.oims.catalog.application.dto;

public record CreateStyleCommand (
    String styleCode,
    String name,
    String category

) {}
