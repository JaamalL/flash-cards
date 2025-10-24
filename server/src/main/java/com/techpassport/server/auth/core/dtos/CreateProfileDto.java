package com.techpassport.server.auth.core.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateProfileDto(
        @NotBlank
        UUID userId,

        String firstName,

        String lastName,

        String avatar,

        String phone,

        String bio
) {}