package com.flashcards.server.flashcards.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.flashcards.server.common.entities.Base;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "tags",
        indexes = {
                @Index(name = "idx_tag_user_id", columnList = "user_id")
        }
)
public class Tag extends Base {
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private final Set<Flashcard> flashcards = new HashSet<>();

    protected Tag() {
    }

    public Tag(UUID userId, String name, String description) {
        super();
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
