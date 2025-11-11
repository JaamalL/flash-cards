package com.flashcards.server.auth.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.flashcards.server.common.entities.Base;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "roles",
        indexes =
                {
                        @Index(name = "idx_roles_name", columnList = "name")
                }
)
public class Role extends Base
{
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    protected Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }
}
