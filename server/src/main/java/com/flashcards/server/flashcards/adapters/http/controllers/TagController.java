package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.CreateTagHandler;
import com.flashcards.server.flashcards.adapters.http.handlers.FindTagByIdHandler;
import com.flashcards.server.flashcards.adapters.http.handlers.FindTagsByUserIdHandler;
import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("tags")
public class TagController {
    private final CreateTagHandler createTagHandler;
    private final FindTagsByUserIdHandler findTagsByUserIdHandler;
    private final FindTagByIdHandler findTagByIdHandler;

    public TagController(
            CreateTagHandler createTagHandler,
            FindTagsByUserIdHandler findTagsByUserIdHandler,
            FindTagByIdHandler findTagByIdHandler
    ) {
        this.createTagHandler = createTagHandler;
        this.findTagsByUserIdHandler = findTagsByUserIdHandler;
        this.findTagByIdHandler = findTagByIdHandler;
    }

    @Authorize
    @PostMapping
    public ResponseEntity<TagDTO> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateTagDTO createTagDTO
    ) {
        return createTagHandler.handle(jwt, createTagDTO);
    }

    @Authorize
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return findTagsByUserIdHandler.handle(jwt);
    }

    @Authorize
    @GetMapping("{tagId}")
    public ResponseEntity<TagDetailsDTO> getById(@PathVariable UUID tagId) {
        return findTagByIdHandler.handle(tagId);
    }
}
