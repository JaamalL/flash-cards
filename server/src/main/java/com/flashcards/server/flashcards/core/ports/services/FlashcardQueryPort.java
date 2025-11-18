package com.flashcards.server.flashcards.core.ports.services;

import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.enums.TagMatchMode;

import java.util.List;
import java.util.UUID;

public interface FlashcardQueryPort {
    List<FlashcardDTO> getByTagIds(UUID uuid, List<UUID> tagIds, TagMatchMode tagMatchMode);
}
