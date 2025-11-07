package com.flashcards.server.auth.infrastructure.repository;

import com.flashcards.server.common.data.repository.BaseRepository;
import jakarta.persistence.NoResultException;
import java.util.Optional;

import com.flashcards.server.auth.core.entities.User;
import com.flashcards.server.auth.core.ports.repository.IUserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository<User> implements IUserRepository {

    public UserRepository() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {

            User entity = em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", type)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
