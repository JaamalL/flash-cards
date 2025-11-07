package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.ISession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RefreshHandler
{
    private final ISession session;

    public RefreshHandler(ISession session) {
        this.session = session;
    }

    public ResponseEntity<Map<String, String>> handle(String refreshToken) {
        var accessToken = session.refreshSession(refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }
}
