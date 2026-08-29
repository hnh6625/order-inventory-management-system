package com.example.oims.catalog.domain.repository;

import com.example.oims.catalog.domain.model.Style;

import java.util.List;
import java.util.Optional;

public interface StyleRepository {
    void save(Style style);
    Optional<Style> findByStyleCode(String styleCode);
    boolean existsByStyleCode(String styleCode);
    List<Style> findAll();
}
