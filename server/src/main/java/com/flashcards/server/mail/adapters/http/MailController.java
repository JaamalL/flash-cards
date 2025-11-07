package com.flashcards.server.mail.adapters.http;

import com.flashcards.server.common.annotation.InternalOnly;
import com.flashcards.server.mail.core.dtos.MailDto;
import com.flashcards.server.mail.core.ports.IMailSender;
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
    private final IMailSender mailSender;

    public  MailController(IMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    @InternalOnly
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMail(@RequestBody MailDto dto)
    {
        mailSender.sendMail(dto.to(), dto.subject(), dto.content());
        return ResponseEntity.ok(Map.of("message", "letter was sent successful"));
    }
}
