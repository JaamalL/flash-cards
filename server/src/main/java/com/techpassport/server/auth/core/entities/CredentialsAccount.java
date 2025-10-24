package com.techpassport.server.auth.core.entities;

import jakarta.persistence.*;

import java.util.UUID;

import com.techpassport.server.auth.core.enums.Provider;

@Entity
@Table(name = "credentials_accounts")
@PrimaryKeyJoinColumn(name = "id")
public class CredentialsAccount extends Account
{
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    protected CredentialsAccount() {}
    public CredentialsAccount(UUID userId, String hashedPassword)
    {
        super(userId, Provider.CREDENTIALS);
        this.hashedPassword = hashedPassword;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }
}
