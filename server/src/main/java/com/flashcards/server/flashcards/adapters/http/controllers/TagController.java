package com.flashcards.server.flashcards.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.flashcards.adapters.http.handlers.CreateTagHandler;
import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
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
@RequestMapping("tags")
public class TagController {
    private final CreateTagHandler createTagHandler;

    public TagController(CreateTagHandler createTagHandler) {
        this.createTagHandler = createTagHandler;
    }

    @Authorize
    @PostMapping
    public ResponseEntity<TagDTO> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateTagDTO createTagDTO
    ) {
        return createTagHandler.handle(jwt, createTagDTO);
    }
}
