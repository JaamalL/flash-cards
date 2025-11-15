package com.flashcards.server.profile.infrastructure.repository;

import com.flashcards.server.common.repository.BaseRepository;
import com.flashcards.server.profile.core.entities.Profile;
import com.flashcards.server.profile.core.ports.repository.IProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfileRepository extends BaseRepository<Profile> implements IProfileRepository {

    public ProfileRepository(@Qualifier("profileManagerFactory") EntityManagerFactory emf) {
        super(Profile.class, emf);
    }

    @Override
    public Optional<Profile> findByUserId(UUID userId) {
        return executeWithEntityManager(em -> {
            try {
                Profile entity = em.createQuery(
                                "SELECT p FROM Profile p WHERE p.userId = :userId", type)
                        .setParameter("userId", userId)
                        .getSingleResult();
                return Optional.of(entity);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }
}
