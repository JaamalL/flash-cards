package com.flashcards.server.auth.core.values;

import com.flashcards.server.auth.core.enums.Role;

import java.time.Duration;
import java.util.UUID;

public class AccessToken extends BaseToken
{
    private final UUID accountId;
    private final Role role;
    private final boolean isVerified;

    public AccessToken(
            UUID userId,
            UUID accountId,
            Duration lifetime,
            Role role,
            boolean isVerified
    )
    {
       super(userId, lifetime);
       this.accountId = accountId;
       this.role = role;
       this.isVerified = isVerified;
    }

    public AccessToken(
            UUID userId,
            UUID accountId,
            Role role,
            boolean isVerified,
            long iat,
            long exp
    ) {
        super(userId, iat, exp);
        this.accountId = accountId;
        this.role = role;
        this.isVerified = isVerified;
    }

    public Role getRole()
    {
        return role;
    }
    public boolean isVerified()
    {
        return isVerified;
    }
    public UUID getAccountId()
    {
        return accountId;
    }
}
