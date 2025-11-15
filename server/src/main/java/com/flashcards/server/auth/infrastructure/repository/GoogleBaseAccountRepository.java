package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.auth.core.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.flashcards.server.auth.core.entities.GoogleAccount;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;

@Repository
public class GoogleBaseAccountRepository
    extends BaseAccountRepository<GoogleAccount> implements IAccountRepository<GoogleAccount>
{
    public GoogleBaseAccountRepository(@Qualifier("authManagerFactory") EntityManagerFactory emf) {
        super(GoogleAccount.class, emf);
    }
}
