package com.techpassport.server.profile.core.services;

import com.techpassport.server.profile.core.dtos.CreateProfileDto;
import com.techpassport.server.profile.core.entites.Profile;
import com.techpassport.server.profile.core.ports.repository.IProfileRepository;
import com.techpassport.server.profile.core.ports.services.ICreateProfile;
import org.springframework.stereotype.Service;

@Service
public class CreateProfile implements ICreateProfile
{
    private final IProfileRepository profileRepository;

    public CreateProfile(IProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile createProfile(CreateProfileDto dto) {
        var profile = new Profile(
                dto.userId(), dto.firstName(), dto.lastName(),
                dto.avatar(), dto.phone(), dto.bio());

        return profileRepository.create(profile);
    }
}
