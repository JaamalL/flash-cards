package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.ports.services.FindTagsPort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindTagByIdHandler {
    private final FindTagsPort findTagsPort;

    public FindTagByIdHandler(FindTagsPort findTagsPort) {
        this.findTagsPort = findTagsPort;
    }

    public ResponseEntity<TagDetailsDTO> handle(UUID tagId) {
        return ResponseEntity.ok(findTagsPort.findById(tagId));
    }
}
