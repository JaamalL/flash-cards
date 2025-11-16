package com.flashcards.server.flashcards.core.dto;

import java.util.UUID;

public record TagDTO(
        UUID id,
        String name,
        String description
) {}
