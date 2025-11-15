package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.common.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository<User> implements IUserRepository
{
    public UserRepository(@Qualifier("authEntityManager") EntityManager authEntityManager) {
        super(User.class, authEntityManager);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {

            User entity = em.createQuery(
                            "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.accounts WHERE u.email = :email",
                            User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
