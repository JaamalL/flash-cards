package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.auth.core.ports.services.IVerify;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VerifyTokenHandler {
    private final IVerify verify;
    private final ISession session;

    public VerifyTokenHandler(IVerify verify, ISession session) {
        this.verify = verify;
        this.session = session;
    }

    public ResponseEntity<Map<String, String>> handle(String token, HttpServletResponse response) {
        var result = verify.verifyUserByToken(token);
        var access = session.refreshSession(result.user().getId(), result.accountId());

        return ResponseEntity.ok(Map.of("accessToken", access));
    }
}