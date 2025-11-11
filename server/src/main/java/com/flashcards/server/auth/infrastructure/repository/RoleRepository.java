package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.auth.core.entities.Role;
import com.flashcards.server.auth.core.ports.repository.IRoleRepository;
import com.flashcards.server.common.data.repository.BaseRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepository extends BaseRepository<Role> implements IRoleRepository
{
    public RoleRepository() {
        super(Role.class);
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
