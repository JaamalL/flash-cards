package com.techpassport.server.auth.core.entities;


import jakarta.persistence.*;

import java.util.UUID;

import com.techpassport.server.auth.core.enums.Provider;
import com.techpassport.server.common.entities.Base;

@Entity
@Table(
        name = "accounts",
        indexes = {
                @Index(name = "idx_accounts_user_id", columnList = "user_id"),
                @Index(name = "idx_accounts_provider", columnList = "provider")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
public class Account extends Base
{
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    protected Account() {}

    public Account(UUID userId, Provider provider) {
        this.userId = userId;
        this.provider = provider;
        this.isVerified = false;
    }

    public UUID getUserId() {
        return userId;
    }

    public Provider getProvider() {
        return provider;
    }

    public boolean isVerified()
    {
        return isVerified;
    }
    public void MarkVerified()
    {
        this.isVerified = true;
    }
}
