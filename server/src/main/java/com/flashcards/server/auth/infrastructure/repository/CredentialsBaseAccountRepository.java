package com.flashcards.server.auth.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.flashcards.server.auth.core.entities.CredentialsAccount;
import com.flashcards.server.auth.core.ports.repository.IAccountRepository;

@Repository
public class CredentialsBaseAccountRepository
    extends BaseAccountRepository<CredentialsAccount>
    implements IAccountRepository<CredentialsAccount>
{
    public CredentialsBaseAccountRepository()
    {
        super(CredentialsAccount.class);
    }
}
