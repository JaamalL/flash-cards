package com.flashcards.server.auth.core.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProfileDto(
        @NotNull
        UUID userId,
        @NotBlank
        String firstName,
        String lastName,
        String avatar,
        String phone,
        String bio
) {}