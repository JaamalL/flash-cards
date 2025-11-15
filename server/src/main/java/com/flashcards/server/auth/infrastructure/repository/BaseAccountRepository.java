package com.flashcards.server.auth.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import com.flashcards.server.common.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseAccountRepository<T extends Account> extends BaseRepository<T> implements IAccountRepository<T> {
    public BaseAccountRepository(Class<T> type, @Qualifier("authEntityManager") EntityManager authEntityManager) {
        super(type, authEntityManager);
    }

    @Override
    public Optional<T> findByUserId(UUID id, Provider provider) {
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
    }

    @Override
    public Account[] findAllAccountsByUserId(UUID id) {
        List<T> results = em.createQuery(
                        "SELECT a FROM " + type.getSimpleName() + " a WHERE a.userId = :id",
                        type)
                .setParameter("id", id)
                .getResultList();
        return results.toArray(new Account[0]);
    }
}


