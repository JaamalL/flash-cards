package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;

import java.util.UUID;

public interface TagManagerPort {
    TagDTO create(CreateTagDTO createTagDTO, UUID userId);
}
