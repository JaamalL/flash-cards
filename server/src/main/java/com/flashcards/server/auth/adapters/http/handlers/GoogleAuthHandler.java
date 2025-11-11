package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.IGoogle;
import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.common.utils.http.request.IHttpRequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

@Component
public class GoogleAuthHandler
{
    @Value("${client.application.url}")
    private String clientUrl;

    private final IGoogle google;
    private final ISession session;
    private final IHttpRequestInfo clientInfo;
    private final CookieFactory cookieFactory;

    public GoogleAuthHandler(IGoogle google, ISession session, IHttpRequestInfo clientInfo, CookieFactory cookieFactory) {
        this.google = google;
        this.session = session;
        this.clientInfo = clientInfo;
        this.cookieFactory = cookieFactory;
    }

    public RedirectView handle(String code, HttpServletRequest request, HttpServletResponse response) {
        var result = google.googleAuth(code);
        var info = clientInfo.getClientInfo(request);
        var tokens = session.createSession(result, info);

        response.addCookie(cookieFactory.refreshToken(tokens.refreshToken()));

        return new RedirectView(clientUrl + "/");
    }
}