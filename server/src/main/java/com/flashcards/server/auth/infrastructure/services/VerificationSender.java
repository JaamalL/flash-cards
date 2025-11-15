package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.dtos.MailDto;
import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.services.ITokenSerializer;
import com.flashcards.server.auth.core.ports.services.IVerificationSender;
import com.flashcards.server.auth.core.ports.services.IVerifyEmailHtmlBuilder;
import com.flashcards.server.auth.core.values.VerifyToken;
import com.flashcards.server.common.redis.core.ports.services.IRedisCache;
import com.flashcards.server.common.utils.generator.ICodeGenerator;
import com.flashcards.server.common.utils.hasher.IPasswordHasher;
import com.flashcards.server.common.utils.http.client.IHttpClient;
import com.flashcards.server.common.utils.redis.IRedisKeyParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class VerificationSender implements IVerificationSender
{
    @Value("${spring.application.url}")
    private String baseUrl;

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
    private final IVerifyEmailHtmlBuilder verifyEmailHtmlBuilder;
    private final IHttpClient httpClient;

    public VerificationSender (
            ITokenSerializer tokenSerializer,
            ICodeGenerator codeGenerator,
            IRedisCache redisCache,
            IRedisKeyParser redisKeyParser,
            IPasswordHasher passwordHasher,
            IVerifyEmailHtmlBuilder verifyEmailHtmlBuilder,
            IHttpClient httpClient
    ) {
        this.tokenSerializer = tokenSerializer;
        this.codeGenerator = codeGenerator;
        this.redisCache = redisCache;
        this.redisKeyParser = redisKeyParser;
        this.passwordHasher = passwordHasher;
        this.verifyEmailHtmlBuilder = verifyEmailHtmlBuilder;
        this.httpClient = httpClient;
    }

    @Override
    public void send(User user, Account account) {
        var verifyTokenPayload = new VerifyToken(user.getId(), account.getId(), verificationTokenLifetime);
        var verificationToken = tokenSerializer.SerializeVerifyToken(verifyTokenPayload);

        var code = codeGenerator.generateCode(6, true);
        var link = String.format("%s/auth/verify/token?verifyToken=%s", baseUrl, verificationToken);
        var lifetime = String.valueOf(verificationLifetime.toMinutes() + " minutes");
        var html = verifyEmailHtmlBuilder.buildVerifyEmailHtml(code, link, lifetime);

        var mailDto = new MailDto(user.getEmail(),String.format("Your verification code is %s", code), html);

        httpClient.postAsync(
                String.format("%s/mail/send", baseUrl),
                mailDto,
                null
        );

        var verificationCodeKey = redisKeyParser.generateVerificationCodeKey(user.getId());
        var verificationAttemptsKey = redisKeyParser.generateVerificationAttemptsKey(user.getId());
        var hashedCode = passwordHasher.hashPassword(code);

        redisCache.setString(verificationCodeKey, hashedCode, verificationCodeLifetime);
        redisCache.setString(verificationAttemptsKey, Integer.toString(verificationCodeMaxAttempts), verificationCodeLifetime);
    }
}
