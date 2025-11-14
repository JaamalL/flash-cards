package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.ports.services.CreateFlashcardPort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateFlashcardHandler {
    private final CreateFlashcardPort createFlashcardPort;

    public CreateFlashcardHandler(CreateFlashcardPort createFlashcardPort) {
        this.createFlashcardPort = createFlashcardPort;
    }

    public ResponseEntity<Map<String, String>> handle(CreateFlashcardDTO createFlashcardDTO) {
        return ResponseEntity.ok(createFlashcardPort.createFlashcard(createFlashcardDTO));
    }
}
