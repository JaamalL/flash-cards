package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.dtos.VerificationCodeDto;
import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.auth.core.ports.services.IVerify;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.UUID;

@Component
public class VerifyCodeHandler {
    private final IVerify verify;
    private final ISession session;

    public VerifyCodeHandler(IVerify verify, ISession session) {
        this.verify = verify;
        this.session = session;
    }

    public ResponseEntity<Map<String, String>> handle(Jwt jwt, VerificationCodeDto dto, HttpServletResponse response) {
        var userId = UUID.fromString(jwt.getSubject());
        var accountId = UUID.fromString(jwt.getClaimAsString("accountId"));

        var result = verify.verifyUserByCode(userId, accountId, dto.verificationCode());
        var access = session.refreshSession(result.user().getId(), result.accountId());

        return ResponseEntity.ok(Map.of("accessToken", access));
    }
}