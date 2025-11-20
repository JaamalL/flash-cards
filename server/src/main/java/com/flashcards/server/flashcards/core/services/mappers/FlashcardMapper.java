package com.flashcards.server.flashcards.core.services.mappers;

import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;

public class FlashcardMapper {
    public static FlashcardDetailsDTO toFlashcardDetailsDTO(Flashcard flashcard) {
        return new FlashcardDetailsDTO(
                flashcard.getId(),
                flashcard.getTextQuestion(),
                flashcard.getUrlQuestion(),
                flashcard.getAnswer(),
                flashcard.getTags().stream().map(Tag::getId).toList(),
                flashcard.getCreatedAt().toString()
        );
    }

    public static FlashcardDTO toFlashcardDTO(Flashcard flashcard) {
        return new FlashcardDTO(
                flashcard.getId(),
                flashcard.getTextQuestion(),
                flashcard.getUrlQuestion(),
                flashcard.getAnswer(),
                flashcard.getTags().stream().map(Tag::getId).toList()
        );
    }
}
