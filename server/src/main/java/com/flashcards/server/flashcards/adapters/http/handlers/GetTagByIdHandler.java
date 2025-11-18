package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.ports.services.TagQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetTagByIdHandler {
    private final TagQueryPort tagQueryPort;

    public GetTagByIdHandler(TagQueryPort tagQueryPort) {
        this.tagQueryPort = tagQueryPort;
    }

    public ResponseEntity<TagDetailsDTO> handle(Jwt jwt, UUID tagId) {
        return ResponseEntity.ok(tagQueryPort.getById(
                UUID.fromString(jwt.getSubject()),
                tagId
        ));
    }
}
