package com.flashcards.server.auth.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.common.repository.BaseRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseAccountRepository<T extends Account> extends BaseRepository<T> implements IAccountRepository<T> {

    public BaseAccountRepository(Class<T> type, @Qualifier("authManagerFactory") EntityManagerFactory emf) {
        super(type, emf);
    }

    @Override
    public Optional<T> findByUserId(UUID id, Provider provider) {
        return executeWithEntityManager(em -> {
            try {
                T entity = em.createQuery(
                                "SELECT a FROM " + type.getSimpleName() + " a WHERE a.userId = :id AND a.provider = :provider",
                                type)
                        .setParameter("id", id)
                        .setParameter("provider", provider)
                        .getSingleResult();
                return Optional.of(entity);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Account[] findAllAccountsByUserId(UUID id) {
        return executeWithEntityManager(em -> {
            List<T> results = em.createQuery(
                            "SELECT a FROM " + type.getSimpleName() + " a WHERE a.userId = :id",
                            type)
                    .setParameter("id", id)
                    .getResultList();
            return results.toArray(new Account[0]);
        });
    }
}
