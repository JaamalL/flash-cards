package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
import com.flashcards.server.flashcards.core.ports.services.FlashcardQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetFlashcardByIdHandler {
    private final FlashcardQueryPort flashcardQueryPort;

    public GetFlashcardByIdHandler(FlashcardQueryPort flashcardQueryPort) {
        this.flashcardQueryPort = flashcardQueryPort;
    }

    public ResponseEntity<FlashcardDetailsDTO> handle(Jwt jwt, UUID flashcardId) {
        return ResponseEntity.ok(flashcardQueryPort.getById(
                UUID.fromString(jwt.getSubject()),
                flashcardId
        ));
    }
}
