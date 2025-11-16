package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.ports.services.CreateTagPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CreateTagHandler {
    private final CreateTagPort createTagPort;

    public CreateTagHandler(CreateTagPort createTagPort) {
        this.createTagPort = createTagPort;
    }

    public ResponseEntity<TagDTO> handle(Jwt jwt, CreateTagDTO createTagDTO) {
        return ResponseEntity.ok(createTagPort.createTag(
                createTagDTO,
                UUID.fromString(jwt.getSubject())
        ));
    }
}
