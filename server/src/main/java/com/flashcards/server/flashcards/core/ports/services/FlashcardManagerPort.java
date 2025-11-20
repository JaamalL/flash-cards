package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;

import java.util.UUID;

public interface FlashcardManagerPort {
    FlashcardDetailsDTO create(CreateFlashcardDTO createFlashcardDTO, UUID userId);
    void deleteById(UUID userId, UUID flashcardId);
}
