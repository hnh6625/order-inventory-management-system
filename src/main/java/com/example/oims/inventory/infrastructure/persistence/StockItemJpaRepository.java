package com.example.oims.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemJpaRepository extends JpaRepository<StockItemJpaEntity, UUID> {
    Optional<StockItemJpaEntity> findBySku(String sku);
    boolean existsBySku(String sku);
}
