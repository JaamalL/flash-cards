package com.techpassport.server.auth.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.techpassport.server.auth.core.entities.CredentialsAccount;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;

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
