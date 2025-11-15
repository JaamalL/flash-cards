package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.auth.core.entities.Role;
import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.repository.IRoleRepository;
import com.flashcards.server.common.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepository extends BaseRepository<Role> implements IRoleRepository
{
    public RoleRepository(@Qualifier("authEntityManager") EntityManager authEntityManager) {
        super(Role.class, authEntityManager);
    }

    @Override
    public Optional<Role> findByName(String name)
    {
        try {
            Role entity = em.createQuery(
                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
