package com.techpassport.server.auth.core.services;

import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.ports.services.ITokenSerializer;
import com.techpassport.server.auth.core.ports.services.IVerificationMailerClient;
import com.techpassport.server.auth.core.ports.services.IVerificationSender;
import com.techpassport.server.auth.core.values.VerificationMailDto;
import com.techpassport.server.auth.core.values.VerifyToken;
import com.techpassport.server.common.data.redis.core.ports.services.IRedisCache;
import com.techpassport.server.common.utils.generator.ICodeGenerator;
import com.techpassport.server.common.utils.hasher.IPasswordHasher;
import com.techpassport.server.common.utils.redis.IRedisKeyParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class VerificationSender implements IVerificationSender
{
    @Value("${auth.tokens.verification-common.lifetime}")
    private Duration verificationLifetime;

    @Value("${auth.tokens.verification-token.lifetime}")
    private Duration verificationTokenLifetime;

    @Value("${auth.tokens.verification-code.lifetime}")
    private Duration verificationCodeLifetime;

    @Value("${auth.tokens.verification-code.max-attempts}")
    private int verificationCodeMaxAttempts;

    private final ITokenSerializer tokenSerializer;
    private final ICodeGenerator codeGenerator;
    private final IRedisCache redisCache;
    private final IRedisKeyParser redisKeyParser;
    private final IPasswordHasher passwordHasher;
    private final IVerificationMailerClient verificationSenserClient;

    public VerificationSender (
            ITokenSerializer tokenSerializer,
            ICodeGenerator codeGenerator,
            IRedisCache redisCache,
            IRedisKeyParser redisKeyParser,
            IPasswordHasher passwordHasher,
            IVerificationMailerClient verificationSenserClient
    ) {
        this.tokenSerializer = tokenSerializer;
        this.codeGenerator = codeGenerator;
        this.redisCache = redisCache;
        this.redisKeyParser = redisKeyParser;
        this.passwordHasher = passwordHasher;
        this.verificationSenserClient = verificationSenserClient;
    }

    @Override
    public void send(User user, Account account)
    {
        var verifyTokenPayload = new VerifyToken(user.getId(), account.getId(), verificationTokenLifetime);
        var verificationToken = tokenSerializer.SerializeVerifyToken(verifyTokenPayload);

        var code = codeGenerator.generateCode(6, true);

        var verificationMailDto = new VerificationMailDto(
                user.getEmail(),
                code,
                verificationToken,
                verificationLifetime
        );

        var verificationCodeKey = redisKeyParser.generateVerificationCodeKey(user.getId());
        var verificationAttemptsKey = redisKeyParser.generateVerificationAttemptsKey(user.getId());
        var hashedCode = passwordHasher.hashPassword(code);

        redisCache.setString(verificationCodeKey, hashedCode, verificationCodeLifetime);
        redisCache.setString(verificationAttemptsKey, Integer.toString(verificationCodeMaxAttempts), verificationCodeLifetime);

        verificationSenserClient.sendVerificationMail(verificationMailDto);
    }
}
