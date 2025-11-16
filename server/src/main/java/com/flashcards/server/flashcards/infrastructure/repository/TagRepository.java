package com.flashcards.server.flashcards.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TagRepository extends BaseRepository<Tag> implements TagRepositoryPort {
    public TagRepository() {
        super(Tag.class);
    }

    @Override
    public List<Tag> findByUserId(UUID userId) {
        try {
            return em.createQuery("SELECT t FROM Tag t WHERE t.userId = :userId", type)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Tag> findAllById(List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return em.createQuery("SELECT t FROM Tag t WHERE t.id IN :tagIds", type)
                    .setParameter("tagIds", tagIds)
                    .getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }
}
