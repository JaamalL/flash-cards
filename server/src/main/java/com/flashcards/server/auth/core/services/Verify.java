package com.flashcards.server.auth.core.services;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.common.data.redis.core.ports.services.IRedisCache;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.common.utils.hasher.PasswordHasher;
import com.flashcards.server.common.utils.redis.IRedisKeyParser;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import com.flashcards.server.auth.core.ports.services.ITokenSerializer;
import com.flashcards.server.auth.core.ports.services.IVerify;
import com.flashcards.server.auth.core.values.AuthResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Verify implements IVerify
{
    private final ITokenSerializer tokenSerializer;
    private final IUserRepository userRepository;
    private final IAccountRepository<Account> accountRepository;
    private final IRedisCache redisCache;
    private final IRedisKeyParser redisKeyParser;
    private final PasswordHasher passwordHasher;

    public Verify(
            ITokenSerializer tokenSerializer,
            IUserRepository userRepository,
            IAccountRepository<Account> accountRepository,
            IRedisCache redisCache,
            IRedisKeyParser redisKeyParser,
            PasswordHasher passwordHasher
    ) {
        this.tokenSerializer = tokenSerializer;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.redisCache = redisCache;
        this.redisKeyParser = redisKeyParser;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void verifyUserByToken(String token) {
        var payload = tokenSerializer.DeserializeVerifyToken(token);

        var user = userRepository.findById(payload.getSub())
                .orElseThrow(() -> new ApiException(new ApiError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found")));

        var account = accountRepository.findById(payload.getAccountId())
                .orElseThrow(() -> new ApiException(new ApiError(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "Account not found")));

        if (account.isVerified())
            throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "ALREADY_VERIFIED", "User already verified"));

        var codeKey = redisKeyParser.generateVerificationCodeKey(user.getId());
        var attemptsKey = redisKeyParser.generateVerificationAttemptsKey(user.getId());

        clearKeys(codeKey, attemptsKey);

        account.MarkVerified();
        accountRepository.update(account.getId(), account);
    }

    @Override
    public AuthResult verifyUserByCode(UUID userId, UUID accountId, String code) {
        var codeKey = redisKeyParser.generateVerificationCodeKey(userId);
        var attemptsKey = redisKeyParser.generateVerificationAttemptsKey(userId);

        var storedCode = redisCache.getString(codeKey);
        if (storedCode == null)
            throw new ApiException(new ApiError(HttpStatus.GONE, "CODE_EXPIRED", "Verify code expired or invalid"));

        var attempts = parseAttempts(attemptsKey);
        if (attempts <= 0) {
            clearKeys(codeKey, attemptsKey);
            throw new ApiException(new ApiError(HttpStatus.TOO_MANY_REQUESTS, "ATTEMPTS_EXCEEDED", "Verification attempts exceeded"));
        }

        if (!passwordHasher.verifyPassword(storedCode, code)) {
            decrementAttempts(attemptsKey, attempts);
            throw new ApiException(new ApiError(HttpStatus.UNAUTHORIZED, "INCORRECT_CODE", "Incorrect code. Attempts left: " + (attempts - 1)));
        }

        clearKeys(codeKey, attemptsKey);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(new ApiError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found")));

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(new ApiError(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "Account not found")));

        if (account.isVerified())
            throw new ApiException(new ApiError(HttpStatus.BAD_REQUEST, "ALREADY_VERIFIED", "User already verified"));

        account.MarkVerified();
        accountRepository.update(account.getId(), account);

        return new AuthResult(user, account.getId());
    }

    private int parseAttempts(String attemptsKey) {
        var str = redisCache.getString(attemptsKey);
        return str != null ? Integer.parseInt(str) : 0;
    }

    private void decrementAttempts(String key, int attempts) {
        var lifetime = redisCache.getLifetime(key);
        redisCache.setString(key, Integer.toString(attempts - 1), lifetime);
    }

    private void clearKeys(String... keys) {
        for (var key : keys) redisCache.delete(key);
    }
}
