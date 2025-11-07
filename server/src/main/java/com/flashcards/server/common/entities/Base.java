package com.flashcards.server.common.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@MappedSuperclass
public abstract class Base
{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    protected UUID id;

    @Column(name = "created_at", nullable = false)
    protected OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    protected OffsetDateTime updatedAt;

    protected Base()
    {}

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
