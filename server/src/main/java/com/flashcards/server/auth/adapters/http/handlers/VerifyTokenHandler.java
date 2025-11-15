package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.ISession;
import com.flashcards.server.auth.core.ports.services.IVerify;
import com.flashcards.server.common.utils.http.request.IHttpRequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

@Component
public class VerifyTokenHandler
{
    @Value("${client.application.url}")
    private String clientUrl;

    private final IVerify verify;
    private final ISession session;
    private final IHttpRequestInfo clientInfo;
    private final CookieFactory cookieFactory;

    public VerifyTokenHandler(
            IVerify verify,
            ISession session,
            IHttpRequestInfo clientInfo,
            CookieFactory cookieFactory
    ) {
        this.verify = verify;
        this.session = session;
        this.clientInfo = clientInfo;
        this.cookieFactory = cookieFactory;
    }

    public RedirectView handle(String token, HttpServletRequest request, HttpServletResponse response) {
        var result = verify.verifyUserByToken(token);
        var info = clientInfo.getClientInfo(request);
        var tokens = session.createSession(result, info);

        response.addCookie(cookieFactory.refreshToken(tokens.refreshToken()));
        response.addCookie(cookieFactory.accessToken(tokens.accessToken()));

        return new RedirectView(clientUrl);
    }
}