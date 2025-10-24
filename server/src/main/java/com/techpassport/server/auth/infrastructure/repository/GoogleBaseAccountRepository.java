package com.techpassport.server.auth.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.techpassport.server.auth.core.entities.GoogleAccount;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;

@Repository
public class GoogleBaseAccountRepository
    extends BaseAccountRepository<GoogleAccount> implements IAccountRepository<GoogleAccount>
{
    public GoogleBaseAccountRepository()
    {
        super(GoogleAccount.class);
    }
}
