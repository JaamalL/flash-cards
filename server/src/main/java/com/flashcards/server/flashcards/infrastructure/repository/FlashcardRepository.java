package com.flashcards.server.flashcards.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import com.flashcards.server.profile.core.entites.Profile;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
