package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.ports.services.TagQueryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindTagByIdHandler {
    private final TagQueryPort tagQueryPort;

    public FindTagByIdHandler(TagQueryPort tagQueryPort) {
        this.tagQueryPort = tagQueryPort;
    }

    public ResponseEntity<TagDetailsDTO> handle(UUID tagId) {
        return ResponseEntity.ok(tagQueryPort.findById(tagId));
    }
}
