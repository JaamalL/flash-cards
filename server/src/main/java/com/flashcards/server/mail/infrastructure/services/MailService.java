package com.flashcards.server.mail.infrastructure.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.mail.core.ports.IMailService;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String from;

    private final MailClient mailClient;

    @Autowired
    public MailService(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Override
    public void sendMail(String to, String subject, String content) {
        MailMessage message = new MailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setHtml(content);

        mailClient.sendMail(message)
                .onSuccess(result -> log.info("Email sent successfully to {}", to))
                .onFailure(ex -> {
                    log.error("Failed to send email to {}", to, ex);
                    throw new ApiException(new ApiError(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "UNEXPECTED_ERROR",
                            "Failed to send email: " + (ex.getMessage() != null ? ex.getMessage() : "unknown error")
                    ));
                });
    }
}
