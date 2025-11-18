package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.CreateFlashcardHandler;
import com.flashcards.server.flashcards.adapters.http.handlers.GetFlashcardsByTagIdsHandler;
import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.enums.TagMatchMode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("flashcards")
public class FlashcardController {
    private final CreateFlashcardHandler createFlashcardHandler;
    private final GetFlashcardsByTagIdsHandler getFlashcardsByTagIdsHandler;

    public FlashcardController(
            CreateFlashcardHandler createFlashcardHandler,
            GetFlashcardsByTagIdsHandler getFlashcardsByTagIdsHandler
    ) {
        this.createFlashcardHandler = createFlashcardHandler;
        this.getFlashcardsByTagIdsHandler = getFlashcardsByTagIdsHandler;
    }

    @Authorize
    @PostMapping
    public ResponseEntity<FlashcardDTO> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateFlashcardDTO createFlashcardDTO
    ) {
        return createFlashcardHandler.handle(jwt, createFlashcardDTO);
    }

    @Authorize
    @GetMapping
    ResponseEntity<List<FlashcardDTO>> getByTagIds(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "matchMode", defaultValue = "ALL") TagMatchMode tagMatchMode,
            @RequestParam(name = "tagId") List<UUID> tagIds
    ) {
        return getFlashcardsByTagIdsHandler.handle(jwt, tagIds, tagMatchMode);
    }
}
