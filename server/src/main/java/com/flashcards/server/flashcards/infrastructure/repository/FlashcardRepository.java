package com.flashcards.server.flashcards.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FlashcardRepository extends BaseRepository<Flashcard> implements FlashcardRepositoryPort {
    public FlashcardRepository() {
        super(Flashcard.class);
    }

    @Override
    public List<Flashcard> findByUserId(UUID userId) {
        return em.createQuery(
                        "SELECT f FROM Flashcard f WHERE f.userId = :userId", type)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Flashcard> findByUserIdAndId(UUID userId, UUID flashcardId) {
        try {
            return Optional.of(em.createQuery("SELECT f FROM Flashcard f " +
                            "WHERE f.userId = :userId AND f.id = :flashcardId", type)
                    .setParameter("userId", userId)
                    .setParameter("flashcardId", flashcardId)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Flashcard> findByUserIdAndAnyTagIds(UUID userId, List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }

        return em.createQuery(
                "SELECT f FROM Flashcard f JOIN f.tags t " +
                        "WHERE f.userId = :userId AND t.id IN :tagIds GROUP BY f.id", type)
                .setParameter("userId", userId)
                .setParameter("tagIds", tagIds)
                .getResultList();
    }

    @Override
    public List<Flashcard> findByUserIdAndAllTagIds(UUID userId, List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }

        return em.createQuery(
                        "SELECT f FROM Flashcard f JOIN f.tags t " +
                                "WHERE f.userId = :userId AND t.id IN :tagIds " +
                                "GROUP BY f.id HAVING COUNT(t.id) = :tagCount", type)
                .setParameter("userId", userId)
                .setParameter("tagIds", tagIds)
                .setParameter("tagCount", tagIds.size())
                .getResultList();
    }
}
