package com.flashcards.server.flashcards.core.dto;

import java.util.List;
import java.util.UUID;

public record FlashcardDTO(
        UUID id,
        String textQuestion,
        String urlQuestion,
        String answer,
        List<UUID> tagIds
) {}
