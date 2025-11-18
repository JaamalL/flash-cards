package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.enums.TagMatchMode;
import com.flashcards.server.flashcards.core.ports.services.FlashcardQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetFlashcardsByTagIdsHandler {
    private final FlashcardQueryPort flashcardQueryPort;

    public GetFlashcardsByTagIdsHandler(FlashcardQueryPort flashcardQueryPort) {
        this.flashcardQueryPort = flashcardQueryPort;
    }

    public ResponseEntity<List<FlashcardDTO>> handle(Jwt jwt, List<UUID> tagIds, TagMatchMode tagMatchMode) {
        return ResponseEntity.ok(flashcardQueryPort.getByTagIds(
                UUID.fromString(jwt.getSubject()),
                tagIds,
                tagMatchMode
        ));
    }
}
