package com.flashcards.server.auth.core.values;

import com.flashcards.server.auth.core.entities.Role;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

public class AccessToken extends BaseToken
{
    private final UUID accountId;
    private final Set<Role> roles;
    private final boolean isVerified;

    public AccessToken(
            UUID userId,
            UUID accountId,
            Duration lifetime,
            Set<Role> roles,
            boolean isVerified
    )
    {
        super(userId, lifetime);
        this.accountId = accountId;
        this.roles = roles;
        this.isVerified = isVerified;
    }

    public AccessToken(
            UUID userId,
            UUID accountId,
            Set<Role> roles,
            boolean isVerified,
            long iat,
            long exp
    ) {
        super(userId, iat, exp);
        this.accountId = accountId;
        this.roles = roles;
        this.isVerified = isVerified;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
