package com.techpassport.server.auth.infrastructure.repository;

import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.auth.core.ports.repository.IAccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository
        extends BaseAccountRepository<Account>
        implements IAccountRepository<Account>
{
    public AccountRepository() {
        super(Account.class);
    }
}
