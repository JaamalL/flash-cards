package com.techpassport.server.auth.core.entities;

import jakarta.persistence.*;

import java.util.UUID;

import com.techpassport.server.auth.core.enums.Provider;

@Entity
@Table(name = "google_accounts")
@PrimaryKeyJoinColumn(name = "id")
public class GoogleAccount extends Account
{
    @Column(name = "sub", nullable = false, unique = true)
    private String sub;

    protected GoogleAccount() {}
    public GoogleAccount(UUID userId, String sub)
    {
        super(userId, Provider.GOOGLE);
        this.sub = sub;
    }

    public String getSub()
    {
        return sub;
    }
}
