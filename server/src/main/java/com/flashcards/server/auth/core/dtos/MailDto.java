package com.flashcards.server.auth.core.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailDto(
        @NotBlank
        @Email
        String to,
        @NotBlank
        String subject,
        @NotBlank
        String content
) {}
