package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import com.techpassport.server.auth.core.values.AuthResult;
import com.techpassport.server.common.exceptions.NotFoundException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

import com.techpassport.server.auth.core.dtos.ClientInfoDto;
import com.techpassport.server.auth.core.ports.repository.IUserRepository;
import com.techpassport.server.auth.core.ports.services.ITokenSerializer;
import com.techpassport.server.auth.core.values.RefreshToken;
import com.techpassport.server.common.data.redis.core.ports.services.IRedisCache;
import com.techpassport.server.common.utils.redis.IRedisKeyParser;
import com.techpassport.server.auth.core.ports.services.ISession;
import com.techpassport.server.auth.core.values.AccessToken;
import com.techpassport.server.auth.core.values.AccessAndRefreshTokens;

@Service
public class Session implements ISession
{
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    @Value("${auth.tokens.max-sessions}")
    private int maxSessionCount;

    @Value("${auth.tokens.access-token.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${auth.tokens.refresh-token.lifetime}")
    private Duration refreshTokenLifetime;

    private final ITokenSerializer tokenSerializer;
    private final IRedisCache redisCache;
    private final IRedisKeyParser redisKeyParser;
    private final IUserRepository userRepository;
    private final IAccountRepository<Account> accountRepository;

    public Session(
            ITokenSerializer tokenSerializer,
            IRedisCache redisCache,
            IRedisKeyParser redisKeyParser,
            IUserRepository userRepository,
            IAccountRepository<Account> accountRepository
    ) {
        this.tokenSerializer = tokenSerializer;
        this.redisCache = redisCache;
        this.redisKeyParser = redisKeyParser;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccessAndRefreshTokens createSession(AuthResult authResult, ClientInfoDto clientInfoDto) {
        var user = authResult.user();
        var account = accountRepository.findById(authResult.accountId())
                .orElseThrow(() -> new NotFoundException("account not found"));

        var accessToken = new AccessToken(
                user.getId(),
                account.getId(),
                accessTokenLifetime,
                user.getRole(),
                account.isVerified()
        );

        var refreshToken = new RefreshToken(
                UUID.randomUUID(),
                user.getId(),
                account.getId(),
                refreshTokenLifetime,
                clientInfoDto.os(),
                clientInfoDto.device(),
                clientInfoDto.ip(),
                clientInfoDto.browser()
        );

        var accessTokenStr = tokenSerializer.SerializeAccessToken(accessToken);
        var refreshTokenStr = tokenSerializer.SerializeRefreshToken(refreshToken);

        var refreshTokenKey = redisKeyParser.generateRefreshTokenKey(refreshToken.getSub(), refreshToken.getId());
        var sessionKey = redisKeyParser.generateSessionKey(refreshToken.getSub());


        redisCache.setString(refreshTokenKey, refreshTokenStr, refreshTokenLifetime);

        var sessionCount = redisCache.getListLength(sessionKey);

        if (sessionCount >= maxSessionCount) {
            var sessions = redisCache.getList(sessionKey, 0, sessionCount - maxSessionCount);

            for (String session : sessions) {
                redisCache.delete(session);
            }
        }

        redisCache.addToList(sessionKey, refreshTokenKey);

        return new AccessAndRefreshTokens(accessTokenStr, refreshTokenStr);
    }

    @Override
    public String refreshSession(String refreshToken) {
        var refreshTokenPayload = tokenSerializer.DeserializeRefreshToken(refreshToken);

        var userId = refreshTokenPayload.getSub();
        var accountId = refreshTokenPayload.getAccountId();

        return refresh(userId, accountId);
    }

    public String refreshSession(UUID userId, UUID accountId) {
        return refresh(userId, accountId);
    }

    private String refresh(UUID userId, UUID accountId)
    {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("account not found"));

        var isExistSession = redisCache.exists(redisKeyParser.generateSessionKey(userId));
        if (!isExistSession) {
            logger.error("Session not found for user {}", userId);
            throw new NotFoundException("Session Not Found");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found for id {}", userId);
                    return new NotFoundException("User Not Found");
                });

        var accessToken = new AccessToken(userId, account.getId(), accessTokenLifetime, user.getRole(), account.isVerified());
        return tokenSerializer.SerializeAccessToken(accessToken);
    }

    @PostConstruct
    public void init() {
        Consumer<String> removeSession = (String key) -> {
            var ids = redisKeyParser.parseRefreshTokenKey(key);
            var sessionKey = redisKeyParser.generateSessionKey(ids.userId());

            redisCache.removeFromList(sessionKey, key, 0);
        };

        redisCache.subscribe("__keyevent@0__:del", removeSession::accept);
        redisCache.subscribe("__keyevent@0__:expired", removeSession::accept);
    }

}
