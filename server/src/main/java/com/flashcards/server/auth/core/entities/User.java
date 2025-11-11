package com.flashcards.server.auth.core.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flashcards.server.common.entities.Base;

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

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Account> accounts = new HashSet<>();

    protected User() {}

    public User(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public Set<Account> getAccounts()
    {
        return accounts;
    }

}
