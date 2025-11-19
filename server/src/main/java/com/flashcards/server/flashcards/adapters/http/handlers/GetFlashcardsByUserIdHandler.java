package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.ports.services.FlashcardQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetFlashcardsByUserIdHandler {
    private final FlashcardQueryPort flashcardQueryPort;

    public GetFlashcardsByUserIdHandler(FlashcardQueryPort flashcardQueryPort) {
        this.flashcardQueryPort = flashcardQueryPort;
    }

    public ResponseEntity<List<FlashcardDTO>> handle(Jwt jwt) {
        return ResponseEntity.ok(flashcardQueryPort.getByUserId(UUID.fromString(jwt.getSubject())));
    }
}
