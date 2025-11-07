package com.flashcards.server.auth.core.values;

public record AccessAndRefreshTokens(
        String accessToken,
        String refreshToken
) {}