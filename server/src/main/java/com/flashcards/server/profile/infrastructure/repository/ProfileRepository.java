package com.flashcards.server.profile.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import com.flashcards.server.profile.core.entites.Profile;
import com.flashcards.server.profile.core.ports.repository.IProfileRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfileRepository extends BaseRepository<Profile> implements IProfileRepository {
    public ProfileRepository() {
        super(Profile.class);
    }

    public Optional<Profile> findByUserId(UUID userId) {
        try {
            Profile entity = em.createQuery(
                            "SELECT p FROM Profile p WHERE p.userId = :userId", type)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(entity);

        } catch (NoResultException e) {
            return Optional.empty();

        }
    }
}
