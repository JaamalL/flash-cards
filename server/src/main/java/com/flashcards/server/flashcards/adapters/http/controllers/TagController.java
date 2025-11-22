package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.*;
import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateTagDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tags")
public class TagController {
    private final CreateTagHandler createTagHandler;
    private final GetTagsByUserIdHandler getTagsByUserIdHandler;
    private final GetTagByIdHandler getTagByIdHandler;
    private final UpdateTagHandler updateTagHandler;
    private final DeleteTagByIdHandler deleteTagByIdHandler;

    public TagController(
            CreateTagHandler createTagHandler,
            GetTagsByUserIdHandler getTagsByUserIdHandler,
            GetTagByIdHandler getTagByIdHandler,
            UpdateTagHandler updateTagHandler,
            DeleteTagByIdHandler deleteTagByIdHandler
    ) {
        this.createTagHandler = createTagHandler;
        this.getTagsByUserIdHandler = getTagsByUserIdHandler;
        this.getTagByIdHandler = getTagByIdHandler;
        this.updateTagHandler = updateTagHandler;
        this.deleteTagByIdHandler = deleteTagByIdHandler;
    }

    @Authorize
    @PostMapping
    public ResponseEntity<TagDetailsDTO> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateTagDTO createTagDTO
    ) {
        return createTagHandler.handle(jwt, createTagDTO);
    }

    @Authorize
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll(@AuthenticationPrincipal Jwt jwt) {
        return getTagsByUserIdHandler.handle(jwt);
    }

    @Authorize
    @GetMapping("{tagId}")
    public ResponseEntity<TagDetailsDTO> getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID tagId
    ) {
        return getTagByIdHandler.handle(jwt, tagId);
    }

    @Authorize
    @PatchMapping("{tagId}")
    public ResponseEntity<TagDetailsDTO> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID tagId,
            @RequestBody UpdateTagDTO updateTagDTO
            ) {
        return updateTagHandler.handle(jwt, tagId, updateTagDTO);
    }

    @Authorize
    @DeleteMapping("{tagId}")
    public ResponseEntity<Void> deleteById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID tagId
    ) {
        return deleteTagByIdHandler.handle(jwt, tagId);
    }
}
