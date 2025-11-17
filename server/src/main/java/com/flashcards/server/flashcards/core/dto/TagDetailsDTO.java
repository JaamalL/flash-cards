package com.flashcards.server.flashcards.core.dto;

import java.util.List;
import java.util.UUID;

public record TagDetailsDTO(
        UUID id,
        String name,
        String description,
        List<UUID> flashcardIds,
        String createdAt
) {}
