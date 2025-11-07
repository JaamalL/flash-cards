package com.flashcards.server.common.utils.redis;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisKeyParser implements IRedisKeyParser
{
    private static final String refreshTokenPrefix = "refresh-token";
    private static final String sessionPrefix = "session";
    private static final String verificationCodePrefix = "verification-code";
    private static final String verificationAttemptsPrefix = "verification-attempts";

    @Override
    public String generateRefreshTokenKey(UUID userId, UUID refreshTokenId)
    {
        return String.format("%s:%s:%s", refreshTokenPrefix, userId, refreshTokenId);
    }

    @Override
    public String generateSessionKey(UUID userId)
    {
        return String.format("%s:%s", sessionPrefix, userId);
    }

    @Override
    public IDs parseRefreshTokenKey(String refreshTokenKey)
    {
        var parts = refreshTokenKey.split(":");
        if (parts.length != 3 || !parts[0].equals(refreshTokenPrefix)) {
            throw new IllegalArgumentException("Invalid refresh token key: " + refreshTokenKey);
        }

        var userId = UUID.fromString(parts[1]);
        var refreshTokenId = UUID.fromString(parts[2]);

        return new IDs(userId, refreshTokenId);
    }

    @Override
    public String generateVerificationCodeKey(UUID userId)
    {
        return String.format("%s:%s", verificationCodePrefix, userId);
    }

    @Override
    public String generateVerificationAttemptsKey(UUID userId)
    {
        return String.format("%s:%s", verificationAttemptsPrefix, userId);
    }
}
