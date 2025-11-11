package com.flashcards.server.auth.core.dtos;

import jakarta.validation.constraints.NotBlank;

public record VerificationCodeDto(
        @NotBlank
        String verificationCode
) {}
