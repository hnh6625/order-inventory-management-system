package com.example.oims.catalog.infrastructure.persistence;

import com.example.oims.catalog.domain.model.Color;
import com.example.oims.catalog.domain.model.Size;
import com.example.oims.catalog.domain.model.Style;
import com.example.oims.shared.Money;

import java.time.LocalDateTime;

public class StyleMapper {

    public static StyleJpaEntity toJpa(Style style) {
        StyleJpaEntity entity = new StyleJpaEntity(
                style.getId(),
                style.getStyleCode(),
                style.getName(),
                style.getCategory(),
                LocalDateTime.now()
        );

        style.getVariants().stream()
                .map(v -> VariantMapper.toJpa(v, entity))
                .forEach(entity.getVariants()::add);

        return entity;
    }

    public static Style toDomain(StyleJpaEntity entity) {
        Style style = new Style(
                entity.getId(),
                entity.getStyleCode(),
                entity.getName(),
                entity.getCategory()
        );

        for (VariantJpaEntity entityVariant : entity.getVariants()) {
            Size size = Size.valueOf(entityVariant.getSize());
            Color color = new Color(entityVariant.getColorCode(),entityVariant.getColorName());
            Money price = new Money(entityVariant.getPrice());

            style.addVariant(size,color,price);
        }
        return style;
    }
}
