package com.flashcards.server.flashcards.core.services.mappers;

import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;

public class TagMapper {
    public static TagDetailsDTO toTagDetailsDTO(Tag tag) {
        return new TagDetailsDTO(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                tag.getFlashcards().stream().map(Flashcard::getId).toList(),
                tag.getCreatedAt().toString()
        );
    }

    public static TagDTO toTagDTO(Tag tag) {
        return new TagDTO(
                tag.getId(),
                tag.getName(),
                tag.getDescription()
        );
    }
}
