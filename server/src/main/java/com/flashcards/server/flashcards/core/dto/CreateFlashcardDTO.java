package com.flashcards.server.flashcards.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateFlashcardDTO(
        @NotNull(message = "TextQuestion cannot be null")
        String textQuestion,

        String urlQuestion,

        @NotNull(message = "Answer cannot be null")
        @NotBlank(message = "Answer cannot be blank")
        String answer,

        @NotNull(message = "TagIds list cannot be null")
        @NotEmpty(message = "TagIds list cannot be empty")
        List<UUID> tagIds
) {}
