package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.ports.services.ILogout;
import com.flashcards.server.auth.core.ports.services.ITokenSerializer;
import com.flashcards.server.common.redis.core.ports.services.IRedisCache;
import com.flashcards.server.common.utils.redis.IRedisKeyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Logout implements ILogout
{
    private static final Logger logger = LoggerFactory.getLogger(Logout.class);

    private final IRedisCache redisCache;
    private final IRedisKeyParser redisKeyParser;
    private final ITokenSerializer tokenSerializer;

    public Logout(
            IRedisCache redisCache,
            IRedisKeyParser redisKeyParser,
            ITokenSerializer tokenSerializer
    ) {
        this.redisCache = redisCache;
        this.redisKeyParser = redisKeyParser;
        this.tokenSerializer = tokenSerializer;
    }

    @Override
    public void deleteSession(String refreshToken)
    {
        var refreshTokenPayload = tokenSerializer.DeserializeRefreshToken(refreshToken);
        var refreshTokenKey = redisKeyParser.generateRefreshTokenKey(refreshTokenPayload.getSub(), refreshTokenPayload.getId());
        redisCache.delete(refreshTokenKey);
    }
}
