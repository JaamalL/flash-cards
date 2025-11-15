package com.flashcards.server.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.flashcards.server.common.entities.Base;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public abstract class BaseRepository<T extends Base> implements IBaseRepository<T> {

    protected final EntityManager em;
    protected final Class<T> type;

    public BaseRepository(Class<T> type, EntityManager em) {
        this.type = type;
        this.em = em;
    }

    @Override
    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(em.find(type, id));
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("FROM " + type.getName(), type)
                .getResultList();
    }

    @Override
    public T create(T entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
            return entity;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public T update(UUID id, T entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T existing = em.find(type, id);
            if (existing == null) {
                throw new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "NOT_FOUND",
                        type.getSimpleName() + " with id " + id + " not found"
                ));
            }
            T merged = em.merge(entity);
            transaction.commit();
            return merged;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public void deleteById(UUID id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T entity = em.find(type, id);
            if (entity == null) {
                throw new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "NOT_FOUND",
                        type.getSimpleName() + " with id " + id + " not found"
                ));
            }
            em.remove(entity);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction.isActive()) transaction.rollback();
            throw ex;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return em.find(type, id) != null;
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM " + type.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}
