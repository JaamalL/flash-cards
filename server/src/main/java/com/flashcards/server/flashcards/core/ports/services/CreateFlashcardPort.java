package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;

import java.util.Map;
import java.util.UUID;

public interface CreateFlashcardPort {
    Map<String, String> createFlashcard(CreateFlashcardDTO createFlashcardDTO, UUID userId);
}
