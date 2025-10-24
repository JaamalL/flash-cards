package com.techpassport.server.mail.infrastructure.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.techpassport.server.mail.core.ports.services.IMailSender;

@Service
public class MailSender implements IMailSender {

    private static final Logger log = LoggerFactory.getLogger(MailSender.class);

    private final JavaMailSender javaMailSender;

    public MailSender(
            JavaMailSender javaMailSender
    ) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(String to, String subject, String content) {
        try {
            var message = javaMailSender.createMimeMessage();

            var helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        }
        catch (Exception e)
        {
            log.error("Failed to send mail to {}", to, e);
        }
    }
}