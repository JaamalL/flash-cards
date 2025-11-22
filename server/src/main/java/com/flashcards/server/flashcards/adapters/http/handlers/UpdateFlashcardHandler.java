package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateFlashcardDTO;
import com.flashcards.server.flashcards.core.ports.services.FlashcardManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateFlashcardHandler {
    private final FlashcardManagerPort flashcardManagerPort;

    public UpdateFlashcardHandler(FlashcardManagerPort flashcardManagerPort) {
        this.flashcardManagerPort = flashcardManagerPort;
    }

    public ResponseEntity<FlashcardDetailsDTO> handle(
            Jwt jwt,
            UUID flashcardId,
            UpdateFlashcardDTO updateFlashcardDTO
    ) {
        return ResponseEntity.ok(flashcardManagerPort.update(
                UUID.fromString(jwt.getSubject()),
                flashcardId,
                updateFlashcardDTO
        ));
    }
}
