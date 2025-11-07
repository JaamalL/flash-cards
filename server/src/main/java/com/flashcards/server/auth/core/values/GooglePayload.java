package com.flashcards.server.auth.core.values;

public record GooglePayload(
        String sub,
        String email,
        boolean emailVerified,
        String givenName,
        String familyName,
        String picture
) {}
