package com.flashcards.server.flashcards.core.dto;

import java.util.List;
import java.util.UUID;

public record UpdateFlashcardDTO(
    String textQuestion,
    String urlQuestion,
    String answer,
    List<UUID> removeTagIds,
    List<UUID> addTagIds
) {}
