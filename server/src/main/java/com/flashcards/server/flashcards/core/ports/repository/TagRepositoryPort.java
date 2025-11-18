package com.flashcards.server.flashcards.core.ports.repository;

import com.flashcards.server.common.data.repository.IBaseRepository;
import com.flashcards.server.flashcards.core.entities.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepositoryPort extends IBaseRepository<Tag> {
    List<Tag> findByUserId(UUID userId);
    Optional<Tag> findByUserIdAndId(UUID userId, UUID tagId);
    List<Tag> findByIds(List<UUID> tagIds);
    long countByUserIdAndIds(UUID userId, List<UUID> tagIds);
}
