package com.flashcards.server.flashcards.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FlashcardRepository extends BaseRepository<Flashcard> implements FlashcardRepositoryPort {
    public FlashcardRepository() {
        super(Flashcard.class);
    }

    @Override
    public List<Flashcard> findByUserId(UUID userId) {
        try {
            return em.createQuery(
                            "SELECT f FROM Flashcard f WHERE f.userId = :userId", type)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }
}
