package com.flashcards.server.auth.core.ports.repository;

import java.util.Optional;

import com.flashcards.server.common.repository.IBaseRepository;
import com.flashcards.server.auth.core.entities.User;

public interface IUserRepository extends IBaseRepository<User> {

    Optional<User> findByEmail(String email);
}
