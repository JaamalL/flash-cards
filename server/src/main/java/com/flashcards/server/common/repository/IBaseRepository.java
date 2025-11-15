package com.flashcards.server.common.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

import com.flashcards.server.common.entities.Base;

@NoRepositoryBean
public interface IBaseRepository<T extends Base> {

    Optional<T> findById(UUID id);

    List<T> findAll();

    T create(T entity);

    T update(UUID id, T entity);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    long count();
}
