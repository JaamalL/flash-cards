package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.IVerify;
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

    public VerifyTokenHandler(IVerify verify) {
        this.verify = verify;
    }

    public RedirectView handle(String token, HttpServletResponse response) {
        verify.verifyUserByToken(token);
        return new RedirectView(clientUrl);
    }
}