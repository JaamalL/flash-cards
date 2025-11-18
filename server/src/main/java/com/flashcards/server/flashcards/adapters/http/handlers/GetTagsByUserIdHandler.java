package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.ports.services.TagQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetTagsByUserIdHandler {
    private final TagQueryPort tagQueryPort;

    public GetTagsByUserIdHandler(TagQueryPort tagQueryPort) {
        this.tagQueryPort = tagQueryPort;
    }

    public ResponseEntity<List<TagDTO>> handle(Jwt jwt) {
        return ResponseEntity.ok(tagQueryPort.getByUserId(
                UUID.fromString(jwt.getSubject())
        ));
    }
}
