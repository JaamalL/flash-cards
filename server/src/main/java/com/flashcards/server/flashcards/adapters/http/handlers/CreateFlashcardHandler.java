package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.ports.services.FlashcardManagerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateFlashcardHandler {
    private final FlashcardManagerPort flashcardManagerPort;

    public CreateFlashcardHandler(FlashcardManagerPort flashcardManagerPort) {
        this.flashcardManagerPort = flashcardManagerPort;
    }

    public ResponseEntity<FlashcardDTO> handle(Jwt jwt, CreateFlashcardDTO createFlashcardDTO) {
        return ResponseEntity.ok(flashcardManagerPort.createFlashcard(
                createFlashcardDTO,
                UUID.fromString(jwt.getSubject())
        ));
    }
}
