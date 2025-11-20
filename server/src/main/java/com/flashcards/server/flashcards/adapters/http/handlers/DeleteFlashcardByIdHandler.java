package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.ports.services.FlashcardManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteFlashcardByIdHandler {
    private final FlashcardManagerPort flashcardManagerPort;

    public DeleteFlashcardByIdHandler(FlashcardManagerPort flashcardManagerPort) {
        this.flashcardManagerPort = flashcardManagerPort;
    }

    public ResponseEntity<Void> handle(Jwt jwt, UUID flashcardId) {
        flashcardManagerPort.deleteById(UUID.fromString(jwt.getSubject()), flashcardId);
        return ResponseEntity.noContent().build();
    }
}
