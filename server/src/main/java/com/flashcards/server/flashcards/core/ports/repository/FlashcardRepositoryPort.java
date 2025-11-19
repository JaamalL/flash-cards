package com.flashcards.server.flashcards.core.ports.repository;

import com.flashcards.server.common.data.repository.IBaseRepository;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlashcardRepositoryPort extends IBaseRepository<Flashcard> {
    List<Flashcard> findByUserId(UUID userId);
    Optional<Flashcard> findByUserIdAndId(UUID userId, UUID flashcardId);
    List<Flashcard> findByUserIdAndAnyTagIds(UUID userId, List<UUID> tagIds);
    List<Flashcard> findByUserIdAndAllTagIds(UUID userId, List<UUID> tagIds);
}
