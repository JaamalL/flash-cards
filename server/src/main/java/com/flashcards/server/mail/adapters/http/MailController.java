package com.flashcards.server.mail.adapters.http;

import com.flashcards.server.common.annotation.InternalOnly;
import com.flashcards.server.mail.core.dtos.MailDto;
import com.flashcards.server.mail.core.ports.IMailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mail")
public class MailController
{
    private static final Logger log = LoggerFactory.getLogger(MailController.class);
    private final IMailService mailService;

    @Autowired
    public MailController(IMailService mailService)
    {
        this.mailService = mailService;
    }

    @InternalOnly
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMail(@RequestBody @Valid MailDto dto)
    {
        try {
            mailService.sendMail(dto.to(), dto.subject(), dto.content());
            return ResponseEntity.ok(Map.of("message", "letter was sent successful"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
