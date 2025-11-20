package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.ports.services.TagManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteTagByIdHandler {
    private final TagManagerPort tagManagerPort;

    public DeleteTagByIdHandler(TagManagerPort tagManagerPort) {
        this.tagManagerPort = tagManagerPort;
    }

    public ResponseEntity<Void> handle(Jwt jwt, UUID tagId) {
        tagManagerPort.deleteById(UUID.fromString(jwt.getSubject()), tagId);
        return ResponseEntity.noContent().build();
    }
}
