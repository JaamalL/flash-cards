package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;

public interface CreateFlashcardPort {
    Flashcard createFlashcard(CreateFlashcardDTO createFlashcardDTO);
}
