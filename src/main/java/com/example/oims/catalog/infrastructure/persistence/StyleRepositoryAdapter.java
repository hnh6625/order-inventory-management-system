package com.example.oims.catalog.infrastructure.persistence;

import com.example.oims.catalog.domain.model.Style;
import com.example.oims.catalog.domain.repository.StyleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StyleRepositoryAdapter implements StyleRepository {
    private final StyleJpaRepository jpaRepository;

    public StyleRepositoryAdapter(StyleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Style style) {
        StyleJpaEntity entity = StyleMapper.toJpa(style);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Style> findByStyleCode(String styleCode) {
        return jpaRepository.findByStyleCode(styleCode)
                .map(StyleMapper::toDomain);
    }

    @Override
    public boolean existsByStyleCode(String styleCode) {
        return jpaRepository.existsByStyleCode(styleCode);
    }

    @Override
    public List<Style> findAll() {
        return jpaRepository.findAll().stream()
                .map(StyleMapper::toDomain)
                .toList();
    }
}
