package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.ILogout;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LogoutHandler {
    private final ILogout logout;
    private final CookieFactory cookieFactory;

    public LogoutHandler(ILogout logout, CookieFactory cookieFactory) {
        this.logout = logout;
        this.cookieFactory = cookieFactory;
    }

    public ResponseEntity<Map<String, String>> handle(String refreshToken, HttpServletResponse response) {
        logout.deleteSession(refreshToken);

        response.addCookie(cookieFactory.deleteAccessToken());
        response.addCookie(cookieFactory.deleteRefreshToken());

        return ResponseEntity.ok(Map.of("message", "Session has been deleted"));
    }
}