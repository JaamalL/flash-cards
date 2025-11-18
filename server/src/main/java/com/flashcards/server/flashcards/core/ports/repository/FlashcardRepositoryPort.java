package com.flashcards.server.flashcards.core.ports.repository;

import com.flashcards.server.common.data.repository.IBaseRepository;
import com.flashcards.server.flashcards.core.entities.Flashcard;

import java.util.List;
import java.util.UUID;

public interface FlashcardRepositoryPort extends IBaseRepository<Flashcard> {
    List<Flashcard> findByUserId(UUID userId);
    List<Flashcard> findByUserIdAndAnyTagIds(UUID userId, List<UUID> tagIds);
    List<Flashcard> findByUserIdAndAllTagIds(UUID userId, List<UUID> tagIds);
}
