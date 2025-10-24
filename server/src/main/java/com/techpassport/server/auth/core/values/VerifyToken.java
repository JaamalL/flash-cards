package com.techpassport.server.auth.core.values;

import java.time.Duration;
import java.util.UUID;

public class VerifyToken extends BaseToken
{
    private final UUID accountId;

    public VerifyToken(
            UUID userId,
            UUID accountId,
            Duration lifetime
    )
    {
        super(userId, lifetime);
        this.accountId = accountId;
    }

    public VerifyToken(
            UUID userId,
            UUID accountId,
            long iat,
            long exp
    ){
        super(userId, iat, exp);
        this.accountId = accountId;
    }

    public UUID getAccountId()
    {
        return accountId;
    }
}
