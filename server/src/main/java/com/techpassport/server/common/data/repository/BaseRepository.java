package com.techpassport.server.common.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.techpassport.server.common.entities.Base;

@Transactional
public abstract class BaseRepository<T extends Base> implements IBaseRepository<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    protected BaseRepository(Class<T> type) {
        this.type = type;
    }

    @Override
    public Optional<T> findById(UUID id) {
        T entity = em.find(type, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("FROM " + type.getName(), type)
                .getResultList();
    }

    @Override
    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T update(UUID id, T entity) {
        T existing = em.find(type, id);
        if (existing == null) {
            throw new EntityNotFoundException(type.getSimpleName() + " with id " + id + " not found");
        }
        return em.merge(entity);
    }

    @Override
    public void deleteById(UUID id) {
        T entity = em.find(type, id);
        if (entity == null) {
            throw new EntityNotFoundException(type.getSimpleName() + " with id " + id + " not found");
        }
        em.remove(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        T entity = em.find(type, id);
        return entity != null;
    }

    @Override
    public long count() {
        return em.createQuery(
                        "SELECT COUNT(e) FROM " + type.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}
