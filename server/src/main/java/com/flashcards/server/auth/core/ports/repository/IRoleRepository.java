package com.flashcards.server.auth.core.ports.repository;

import java.util.Optional;

import com.flashcards.server.auth.core.entities.Role;
import com.flashcards.server.common.data.repository.IBaseRepository;

public interface IRoleRepository extends IBaseRepository<Role>
{
    Optional<Role> findByName(String name);
}
