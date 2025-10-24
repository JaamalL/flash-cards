package com.techpassport.server.auth.core.ports.repository;

import java.util.Optional;
import java.util.UUID;

import com.techpassport.server.auth.core.enums.Provider;
import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.common.data.repository.IBaseRepository;

public interface IAccountRepository<T extends Account> extends IBaseRepository<T> {

    Optional<T> findByUserId(UUID id, Provider provider);

    Account[] findAllAccountsByUserId(UUID id);
}
