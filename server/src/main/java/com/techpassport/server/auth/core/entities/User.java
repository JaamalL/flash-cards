package com.techpassport.server.auth.core.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.techpassport.server.common.entities.Base;
import com.techpassport.server.auth.core.enums.Role;

@Entity
@Table(
    name = "users",
    indexes =
    {
        @Index(name = "idx_users_email", columnList = "email")
    }
)
public class User extends Base
{
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Account> accounts = new ArrayList<>();

    protected User() {}

    public User(String email)
    {
        this.email = email;
        this.role = Role.USER;
    }

    public String getEmail()
    {
        return email;
    }

    public Role getRole()
    {
        return role;
    }
    public void setRole(Role role)
    {
        this.role = role;
    }

    public List<Account> getAccounts()
    {
        return accounts;
    }

}
