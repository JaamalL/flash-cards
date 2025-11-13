package com.flashcards.server.mail.core.ports;

public interface IMailService
{
    void sendMail(String to, String subject, String content);
}
