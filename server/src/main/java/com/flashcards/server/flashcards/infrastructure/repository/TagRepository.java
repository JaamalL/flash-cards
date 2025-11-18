package com.flashcards.server.flashcards.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TagRepository extends BaseRepository<Tag> implements TagRepositoryPort {
    public TagRepository() {
        super(Tag.class);
    }

    @Override
    public List<Tag> findByUserId(UUID userId) {
        return em.createQuery("SELECT t FROM Tag t WHERE t.userId = :userId", type)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Tag> findByUserIdAndId(UUID userId, UUID tagId) {
        try {
            return Optional.of(em.createQuery("SELECT t FROM Tag t " +
                            "WHERE t.userId = :userId AND t.id = :tagId", type)
                    .setParameter("userId", userId)
                    .setParameter("tagId", tagId)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findByIds(List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }

        return em.createQuery("SELECT t FROM Tag t WHERE t.id IN :tagIds", type)
                .setParameter("tagIds", tagIds)
                .getResultList();
    }

    @Override
    public long countByUserIdAndIds(UUID userId, List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return 0;
        }

        return em.createQuery(
                "SELECT COUNT(t.id) FROM Tag t " +
                        "WHERE t.id IN :tagIds AND t.userId = :userId", Long.class)
                .setParameter("tagIds", tagIds)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
