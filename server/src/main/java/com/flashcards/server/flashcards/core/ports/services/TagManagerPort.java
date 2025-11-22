package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateTagDTO;

import java.util.UUID;

public interface TagManagerPort {
    TagDetailsDTO create(CreateTagDTO createTagDTO, UUID userId);
    TagDetailsDTO update(UUID userId, UUID tagId, UpdateTagDTO updateTagDTO);
    void deleteById(UUID userId, UUID tagId);
}
