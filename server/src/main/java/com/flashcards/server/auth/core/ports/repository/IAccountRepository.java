package com.flashcards.server.auth.core.ports.repository;

import java.util.Optional;
import java.util.UUID;

import com.flashcards.server.auth.core.enums.Provider;
import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.common.repository.IBaseRepository;

public interface IAccountRepository<T extends Account> extends IBaseRepository<T> {

    Optional<T> findByUserId(UUID id, Provider provider);

    Account[] findAllAccountsByUserId(UUID id);
}
