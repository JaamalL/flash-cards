package com.flashcards.server.flashcards.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFlashcardDTO(
        @NotNull(message = "TextQuestion cannot be null")
        String textQuestion,

        String urlQuestion,

        @NotNull(message = "Answer cannot be null")
        @NotBlank(message = "Answer cannot be blank")
        String answer
) {}
