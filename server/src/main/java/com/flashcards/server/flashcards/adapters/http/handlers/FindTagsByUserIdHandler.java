package com.flashcards.server.flashcards.adapters.http.handlers;

import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.ports.services.FindTagsPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class FindTagsByUserIdHandler {
    private final FindTagsPort findTagsPort;

    public FindTagsByUserIdHandler(FindTagsPort findTagsPort) {
        this.findTagsPort = findTagsPort;
    }

    public ResponseEntity<List<TagDTO>> handle(Jwt jwt) {
        return ResponseEntity.ok(findTagsPort.findByUserId(
                UUID.fromString(jwt.getSubject())
        ));
    }
}
