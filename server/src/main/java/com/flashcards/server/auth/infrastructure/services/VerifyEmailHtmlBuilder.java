package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.ports.services.IVerifyEmailHtmlBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class VerifyEmailHtmlBuilder implements IVerifyEmailHtmlBuilder
{
    private final TemplateEngine templateEngine;

    public VerifyEmailHtmlBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String buildVerifyEmailHtml(String code, String link, String lifetime) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("link", link);
        context.setVariable("lifetime", lifetime);
        return templateEngine.process("verify_email", context);
    }
}
