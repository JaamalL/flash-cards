package com.flashcards.server.auth.core.dtos;

import jakarta.validation.constraints.Email;

public record LoginDto
(
    @Email
    String email,

    String password
) {}