package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;

import java.util.UUID;

public interface FlashcardManagerPort {
    FlashcardDTO create(CreateFlashcardDTO createFlashcardDTO, UUID userId);
}
