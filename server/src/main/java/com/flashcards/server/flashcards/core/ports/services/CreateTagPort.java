package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;

import java.util.Map;
import java.util.UUID;

public interface CreateTagPort {
    TagDTO createTag(CreateTagDTO createTagDTO, UUID userId);
}
