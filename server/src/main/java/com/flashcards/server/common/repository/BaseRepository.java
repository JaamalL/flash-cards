package com.flashcards.server.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.flashcards.server.common.entities.Base;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public abstract class BaseRepository<T extends Base> implements IBaseRepository<T> {

    protected final EntityManagerFactory emf;
    protected final Class<T> type;

    public BaseRepository(Class<T> type, EntityManagerFactory emf) {
        this.type = type;
        this.emf = emf;
    }

    @Override
    public Optional<T> findById(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(type, id));
        }
    }

    @Override
    public List<T> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("FROM " + type.getName(), type)
                    .getResultList();
        }
    }

    @Override
    public T create(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(entity);
                tx.commit();
                return entity;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public T update(UUID id, T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                T existing = em.find(type, id);
                if (existing == null) {
                    throw new ApiException(new ApiError(
                            HttpStatus.NOT_FOUND,
                            "NOT_FOUND",
                            type.getSimpleName() + " with id " + id + " not found"
                    ));
                }
                T merged = em.merge(entity);
                tx.commit();
                return merged;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                T entity = em.find(type, id);
                if (entity == null) {
                    throw new ApiException(new ApiError(
                            HttpStatus.NOT_FOUND,
                            "NOT_FOUND",
                            type.getSimpleName() + " with id " + id + " not found"
                    ));
                }
                em.remove(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public boolean existsById(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(type, id) != null;
        }
    }

    @Override
    public long count() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT COUNT(e) FROM " + type.getSimpleName() + " e", Long.class)
                    .getSingleResult();
        }
    }

    protected <R> R executeWithEntityManager(EntityManagerCallback<R> callback) {
        try (EntityManager em = emf.createEntityManager()) {
            return callback.doInEntityManager(em);
        }
    }

    @FunctionalInterface
    public interface EntityManagerCallback<R> {
        R doInEntityManager(EntityManager em);
    }
}
