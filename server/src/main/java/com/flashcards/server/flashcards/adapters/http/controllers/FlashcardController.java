package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.CreateFlashcardHandler;
import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("flashcards")
public class FlashcardController {
    private final CreateFlashcardHandler createFlashcardHandler;

    public FlashcardController(CreateFlashcardHandler createFlashcardHandler) {
        this.createFlashcardHandler = createFlashcardHandler;
    }

    @Authorize
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateFlashcardDTO createFlashcardDTO
    ) {
        return createFlashcardHandler.handle(jwt, createFlashcardDTO);
    }
}
