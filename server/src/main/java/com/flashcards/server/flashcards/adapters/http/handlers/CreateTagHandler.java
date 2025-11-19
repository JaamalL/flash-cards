package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.ports.services.TagManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateTagHandler {
    private final TagManagerPort tagManagerPort;

    public CreateTagHandler(TagManagerPort tagManagerPort) {
        this.tagManagerPort = tagManagerPort;
    }

    public ResponseEntity<TagDTO> handle(Jwt jwt, CreateTagDTO createTagDTO) {
        return ResponseEntity.ok(tagManagerPort.createTag(
                createTagDTO,
                UUID.fromString(jwt.getSubject())
        ));
    }
}
