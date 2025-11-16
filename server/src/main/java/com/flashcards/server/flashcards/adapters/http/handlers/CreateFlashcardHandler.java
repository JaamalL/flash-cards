package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.ports.services.CreateFlashcardPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CreateFlashcardHandler {
    private final CreateFlashcardPort createFlashcardPort;

    public CreateFlashcardHandler(CreateFlashcardPort createFlashcardPort) {
        this.createFlashcardPort = createFlashcardPort;
    }

    public ResponseEntity<FlashcardDTO> handle(Jwt jwt, CreateFlashcardDTO createFlashcardDTO) {
        return ResponseEntity.ok(createFlashcardPort.createFlashcard(
                createFlashcardDTO,
                UUID.fromString(jwt.getSubject())
        ));
    }
}
