package com.flashcards.server.flashcards.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.flashcards.server.common.entities.Base;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flashcard_tags")
public class FlashcardTag extends Base {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private final Set<Flashcard> flashcards = new HashSet<>();

    protected FlashcardTag() {
    }

    public FlashcardTag(String name, String description) {
        this.name = name;
        this.description = description;
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
