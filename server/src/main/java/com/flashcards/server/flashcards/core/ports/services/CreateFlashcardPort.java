package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;

import java.util.Map;

public interface CreateFlashcardPort {
    Map<String, String> createFlashcard(CreateFlashcardDTO createFlashcardDTO);
}
