package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.auth.core.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.flashcards.server.auth.core.entities.CredentialsAccount;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;

@Repository
public class CredentialsBaseAccountRepository
    extends BaseAccountRepository<CredentialsAccount>
    implements IAccountRepository<CredentialsAccount>
{
    public CredentialsBaseAccountRepository(@Qualifier("authEntityManager") EntityManager authEntityManager) {
        super(CredentialsAccount.class, authEntityManager);
    }
}
