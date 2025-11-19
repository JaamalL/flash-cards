package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;

import java.util.UUID;

public interface TagManagerPort {
    TagDetailsDTO create(CreateTagDTO createTagDTO, UUID userId);
}
