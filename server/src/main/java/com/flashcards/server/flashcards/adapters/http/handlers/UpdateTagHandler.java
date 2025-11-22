package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateTagDTO;
import com.flashcards.server.flashcards.core.ports.services.TagManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateTagHandler {
    private final TagManagerPort tagManagerPort;

    public UpdateTagHandler(TagManagerPort tagManagerPort) {
        this.tagManagerPort = tagManagerPort;
    }

    public ResponseEntity<TagDetailsDTO> handle(Jwt jwt, UUID tagId, UpdateTagDTO updateTagDTO) {
        return ResponseEntity.ok(tagManagerPort.update(
                UUID.fromString(jwt.getSubject()),
                tagId,
                updateTagDTO
        ));
    }
}
