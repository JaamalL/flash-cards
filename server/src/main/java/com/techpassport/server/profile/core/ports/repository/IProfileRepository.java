package com.techpassport.server.profile.core.ports.repository;

import com.techpassport.server.common.data.repository.IBaseRepository;
import com.techpassport.server.profile.core.entites.Profile;

import java.util.Optional;
import java.util.UUID;

public interface IProfileRepository extends IBaseRepository<Profile> {
    Optional<Profile> findByUserId(UUID userId);
}
