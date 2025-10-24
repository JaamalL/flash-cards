package com.techpassport.server.auth.core.ports.repository;

import java.util.Optional;

import com.techpassport.server.common.data.repository.IBaseRepository;
import com.techpassport.server.auth.core.entities.User;

public interface IUserRepository extends IBaseRepository<User> {

    Optional<User> findByEmail(String email);
}
