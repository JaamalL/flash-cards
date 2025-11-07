package com.flashcards.server.profile.core.dtos;

import java.util.UUID;

public record CreateProfileDto(
        UUID userId,
        String firstName,
        String lastName,
        String avatar,
        String phone,
        String bio
) {}