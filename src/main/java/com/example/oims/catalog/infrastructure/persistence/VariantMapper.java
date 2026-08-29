package com.example.oims.catalog.infrastructure.persistence;

import com.example.oims.catalog.domain.model.Variant;

public class VariantMapper {
    public static VariantJpaEntity toJpa(Variant variant, StyleJpaEntity styleEntity) {
        VariantJpaEntity jpaEntity = new VariantJpaEntity(
                variant.getId(),
                variant.getSKU().getValue(),
                variant.getSize().name(),
                variant.getColor().getCode(),
                variant.getColor().getName(),
                variant.getPrice().getAmount(),
                styleEntity
        );
        return jpaEntity;
    }
}
