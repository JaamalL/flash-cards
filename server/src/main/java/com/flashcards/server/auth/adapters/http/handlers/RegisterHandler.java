package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.dtos.RegisterDto;
import com.flashcards.server.auth.core.ports.services.IRegister;
import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.common.utils.http.IHttpClientDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegisterHandler {
    private final IRegister register;
    private final ISession session;
    private final IHttpClientDetails clientInfo;
    private final CookieFactory cookieFactory;

    public RegisterHandler(IRegister register, ISession session, IHttpClientDetails clientInfo, CookieFactory cookieFactory) {
        this.register = register;
        this.session = session;
        this.clientInfo = clientInfo;
        this.cookieFactory = cookieFactory;
    }

    public ResponseEntity<Map<String, String>> handle(RegisterDto dto, HttpServletRequest request, HttpServletResponse response) {
        var user = register.registerUserByCredentials(dto);
        var info = clientInfo.getClientInfo(request);
        var tokens = session.createSession(user, info);

        response.addCookie(cookieFactory.refreshToken(tokens.refreshToken()));

        return ResponseEntity.ok(Map.of(
                "accessToken", tokens.accessToken(),
                "refreshToken", tokens.refreshToken()
        ));
    }
}