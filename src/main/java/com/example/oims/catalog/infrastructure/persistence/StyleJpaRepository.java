package com.example.oims.catalog.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StyleJpaRepository extends JpaRepository<StyleJpaEntity, UUID> {
    Optional<StyleJpaEntity> findByStyleCode(String styleCode);
    boolean existsByStyleCode(String styleCode);
}
