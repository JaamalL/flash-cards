package com.flashcards.server.common.utils.redis;

import java.util.UUID;

public interface IRedisKeyParser
{
    String generateRefreshTokenKey(UUID userId, UUID refreshTokenId);
    String generateSessionKey(UUID userId);

    IDs parseRefreshTokenKey(String refreshTokenKey);

    String generateVerificationCodeKey(UUID userId);
    String generateVerificationAttemptsKey(UUID userId);
}
