package com.flashcards.server.mail.core.ports;

public interface IMailSender
{
    void sendMail(String to, String subject, String content);
}
