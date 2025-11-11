package com.flashcards.server.mail.infrastructure.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.flashcards.server.mail.core.ports.IMailSender;

import java.util.concurrent.TimeUnit;

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
        catch (Exception exception)
        {
            throw new ApiException(new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "UNEXPECTED_ERROR",
                    "Failed to send email: " + (exception.getMessage() != null ? exception.getMessage() : "unknown error")
            ));
        }
    }
}