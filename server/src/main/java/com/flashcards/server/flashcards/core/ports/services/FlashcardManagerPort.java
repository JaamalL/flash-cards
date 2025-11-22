package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.*;

import java.util.UUID;

public interface FlashcardManagerPort {
    FlashcardDetailsDTO create(CreateFlashcardDTO createFlashcardDTO, UUID userId);
    FlashcardDetailsDTO update(UUID userId, UUID flashcardId, UpdateFlashcardDTO updateFlashcardDTO);
    void deleteById(UUID userId, UUID flashcardId);
}
