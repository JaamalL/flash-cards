package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.auth.core.entities.GoogleAccount;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import com.techpassport.server.common.data.redis.core.ports.services.IRedisCache;
import com.techpassport.server.common.exceptions.NotFoundException;
import com.techpassport.server.common.utils.hasher.PasswordHasher;
import com.techpassport.server.common.utils.redis.IRedisKeyParser;
import com.techpassport.server.auth.core.ports.repository.IUserRepository;
import com.techpassport.server.auth.core.ports.services.ITokenSerializer;
import com.techpassport.server.auth.core.ports.services.IVerification;
import com.techpassport.server.auth.core.values.AuthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Verification implements IVerification {

    private static final Logger log = LoggerFactory.getLogger(Verification.class);

    private final ITokenSerializer tokenSerializer;
    private final IUserRepository userRepository;
    private final IAccountRepository<Account> accountRepository;
    private final IRedisCache redisCache;
    private final IRedisKeyParser redisKeyParser;
    private final PasswordHasher passwordHasher;

    public Verification(
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
    public AuthResult verifyUserByToken(String token) {
        try {
            var payload = tokenSerializer.DeserializeVerifyToken(token);

            var user = userRepository.findById(payload.getSub())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            var account = accountRepository.findById(payload.getAccountId())
                    .orElseThrow(() -> new NotFoundException("Account not found"));

            if (account.isVerified())
                throw new IllegalArgumentException("User already verified");

            var codeKey = redisKeyParser.generateVerificationCodeKey(user.getId());
            var attemptsKey = redisKeyParser.generateVerificationAttemptsKey(user.getId());

            clearKeys(codeKey, attemptsKey);

            account.MarkVerified();
            accountRepository.update(account.getId(), account);

            return new AuthResult(user, account.getId());
        } catch (Exception e) {
            log.error("Error verifying user by token: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuthResult verifyUserByCode(UUID userId, UUID accountId, String code) {
        try {
            var codeKey = redisKeyParser.generateVerificationCodeKey(userId);
            var attemptsKey = redisKeyParser.generateVerificationAttemptsKey(userId);

            var storedCode = redisCache.getString(codeKey);
            if (storedCode == null)
                throw new IllegalArgumentException("Verification code expired or invalid");

            var attempts = parseAttempts(attemptsKey);
            if (attempts <= 0) {
                clearKeys(codeKey, attemptsKey);
                throw new IllegalArgumentException("Verification attempts exceeded");
            }

            if (!passwordHasher.verifyPassword(storedCode, code)) {
                decrementAttempts(attemptsKey, attempts);
                throw new IllegalArgumentException("Incorrect code. Attempts left: " + (attempts - 1));
            }

            clearKeys(codeKey, attemptsKey);

            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            var account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException("Account not found"));

            if (account.isVerified())
                throw new IllegalArgumentException("User already verified");

            account.MarkVerified();
            accountRepository.update(account.getId(), account);

            return new AuthResult(user, account.getId());
        } catch (Exception e) {
            log.error("Error verifying user by code: {}", e.getMessage(), e);
            throw e;
        }
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
