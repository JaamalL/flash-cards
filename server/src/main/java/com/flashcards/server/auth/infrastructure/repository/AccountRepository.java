package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository
        extends BaseAccountRepository<Account>
        implements IAccountRepository<Account>
{
    public AccountRepository(@Qualifier("authManagerFactory") EntityManagerFactory emf) {
        super(Account.class, emf);
    }
}
