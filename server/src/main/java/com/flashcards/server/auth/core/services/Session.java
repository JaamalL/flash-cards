package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.auth.core.values.AuthResult;
import com.flashcards.server.common.dtos.ClientInfoDto;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.ITokenSerializer;
import com.flashcards.server.auth.core.values.RefreshToken;
import com.flashcards.server.common.redis.core.ports.services.IRedisCache;
import com.flashcards.server.common.utils.redis.IRedisKeyParser;
import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.auth.core.values.AccessToken;
import com.flashcards.server.auth.core.values.AccessAndRefreshTokens;

@Service
public class Session implements ISession {

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
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "ACCOUNT_NOT_FOUND",
                        "Account not found"
                )));

        var accessToken = new AccessToken(
                user.getId(),
                account.getId(),
                accessTokenLifetime,
                user.getRoles(),
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
        var payload = tokenSerializer.DeserializeRefreshToken(refreshToken);
        return refresh(payload.getSub(), payload.getAccountId(), payload.getId());
    }

    public String refreshSession(UUID userId, UUID accountId, UUID refreshTokenId) {
        return refresh(userId, accountId, refreshTokenId);
    }

    private String refresh(UUID userId, UUID accountId, UUID refreshTokenId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "ACCOUNT_NOT_FOUND",
                        "Account not found"
                )));

        var sessionExists = redisCache.exists(redisKeyParser.generateRefreshTokenKey(userId, refreshTokenId));
        if (!sessionExists) {
            throw new ApiException(new ApiError(
                    HttpStatus.NOT_FOUND,
                    "SESSION_NOT_FOUND",
                    "Session not found for user"
            ));
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User not found"
                )));

        var accessToken = new AccessToken(
                userId,
                account.getId(),
                accessTokenLifetime,
                user.getRoles(),
                account.isVerified()
        );

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
