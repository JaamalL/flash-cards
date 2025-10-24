package com.techpassport.server.mail.core.ports.services;

public interface IMailSender
{
    void sendMail(String to, String subject, String content);
}
