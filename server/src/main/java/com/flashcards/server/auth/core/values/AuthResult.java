package com.flashcards.server.auth.core.values;

import java.util.UUID;

import com.flashcards.server.auth.core.entities.User;

public record AuthResult(User user, UUID accountId) {}