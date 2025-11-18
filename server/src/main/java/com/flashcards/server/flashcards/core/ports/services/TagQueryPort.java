package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface TagQueryPort {
    List<TagDTO> getByUserId(UUID userId);
    TagDetailsDTO getById(UUID userId, UUID tagId);
}
