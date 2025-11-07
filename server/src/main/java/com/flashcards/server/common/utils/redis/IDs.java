package com.flashcards.server.common.utils.redis;

import java.util.UUID;

public record IDs(UUID userId, UUID refreshTokenId) {}