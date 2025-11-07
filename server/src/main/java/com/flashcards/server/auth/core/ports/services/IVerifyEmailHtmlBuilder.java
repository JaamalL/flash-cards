package com.flashcards.server.auth.core.ports.services;

public interface IVerifyEmailHtmlBuilder
{
    String buildVerifyEmailHtml(String code, String link, String lifetime);
}
