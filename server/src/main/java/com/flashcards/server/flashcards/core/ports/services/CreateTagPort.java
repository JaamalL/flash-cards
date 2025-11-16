package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;

import java.util.Map;
import java.util.UUID;

public interface CreateTagPort {
    Map<String, String> createTag(CreateTagDTO createTagDTO, UUID userId);
}
