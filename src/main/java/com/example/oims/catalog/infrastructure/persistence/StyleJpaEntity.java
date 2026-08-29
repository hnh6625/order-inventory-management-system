package com.example.oims.catalog.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "styles")
public class StyleJpaEntity {
    @Id
    private UUID id;

    @Column(name = "style_code", nullable = false, unique = true)
    private String styleCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "style",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<VariantJpaEntity> variants = new ArrayList<>();

    protected StyleJpaEntity() {}

    public StyleJpaEntity(UUID id, String styleCode, String name,
                          String category, LocalDateTime createdAt) {
        this.id = id;
        this.styleCode = styleCode;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<VariantJpaEntity> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantJpaEntity> variants) {
        this.variants = variants;
    }
}
