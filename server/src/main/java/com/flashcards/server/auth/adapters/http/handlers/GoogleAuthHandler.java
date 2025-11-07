package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.IGoogle;
import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.common.utils.http.IHttpClientDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleAuthHandler {
    private final IGoogle google;
    private final ISession session;
    private final IHttpClientDetails clientInfo;
    private final CookieFactory cookieFactory;

    public GoogleAuthHandler(IGoogle google, ISession session, IHttpClientDetails clientInfo, CookieFactory cookieFactory) {
        this.google = google;
        this.session = session;
        this.clientInfo = clientInfo;
        this.cookieFactory = cookieFactory;
    }

    public ResponseEntity<Map<String, String>> handle(String code, HttpServletRequest request, HttpServletResponse response) {
        var result = google.googleAuth(code);
        var info = clientInfo.getClientInfo(request);
        var tokens = session.createSession(result, info);

        response.addCookie(cookieFactory.refreshToken(tokens.refreshToken()));

        return ResponseEntity.ok(Map.of("accessToken", tokens.accessToken()));
    }
}