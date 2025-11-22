package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.*;
import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateFlashcardDTO;
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
    private final GetFlashcardsByUserIdHandler getFlashcardsByUserIdHandler;
    private final GetFlashcardByIdHandler getFlashcardByIdHandler;
    private final GetFlashcardsByTagIdsHandler getFlashcardsByTagIdsHandler;
    private final UpdateFlashcardHandler updateFlashcardHandler;
    private final DeleteFlashcardByIdHandler deleteFlashcardByIdHandler;

    public FlashcardController(
            CreateFlashcardHandler createFlashcardHandler,
            GetFlashcardsByUserIdHandler getFlashcardsByUserIdHandler,
            GetFlashcardByIdHandler getFlashcardByIdHandler,
            GetFlashcardsByTagIdsHandler getFlashcardsByTagIdsHandler,
            UpdateFlashcardHandler updateFlashcardHandler,
            DeleteFlashcardByIdHandler deleteFlashcardByIdHandler
    ) {
        this.createFlashcardHandler = createFlashcardHandler;
        this.getFlashcardsByUserIdHandler = getFlashcardsByUserIdHandler;
        this.getFlashcardByIdHandler = getFlashcardByIdHandler;
        this.getFlashcardsByTagIdsHandler = getFlashcardsByTagIdsHandler;
        this.updateFlashcardHandler = updateFlashcardHandler;
        this.deleteFlashcardByIdHandler = deleteFlashcardByIdHandler;
    }

    @Authorize
    @PostMapping
    public ResponseEntity<FlashcardDetailsDTO> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateFlashcardDTO createFlashcardDTO
    ) {
        return createFlashcardHandler.handle(jwt, createFlashcardDTO);
    }

    @Authorize
    @GetMapping
    public ResponseEntity<List<FlashcardDTO>> get(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "matchMode", defaultValue = "ALL") TagMatchMode tagMatchMode,
            @RequestParam(name = "tagId", required = false) List<UUID> tagIds
    ) {
        if (tagIds == null || tagIds.isEmpty()) {
            return getFlashcardsByUserIdHandler.handle(jwt);
        }

        return getFlashcardsByTagIdsHandler.handle(jwt, tagIds, tagMatchMode);
    }

    @Authorize
    @GetMapping("{flashcardId}")
    public ResponseEntity<FlashcardDetailsDTO> getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID flashcardId
    ) {
        return getFlashcardByIdHandler.handle(jwt, flashcardId);
    }

    @Authorize
    @PatchMapping("{flashcardId}")
    public ResponseEntity<FlashcardDetailsDTO> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID flashcardId,
            @RequestBody UpdateFlashcardDTO updateFlashcardDTO
    ) {
        return updateFlashcardHandler.handle(jwt, flashcardId, updateFlashcardDTO);
    }

    @Authorize
    @DeleteMapping("{flashcardId}")
    public ResponseEntity<Void> deleteById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID flashcardId
    ) {
        return deleteFlashcardByIdHandler.handle(jwt, flashcardId);
    }
}
