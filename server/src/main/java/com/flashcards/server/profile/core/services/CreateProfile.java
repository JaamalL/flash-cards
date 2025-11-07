package com.flashcards.server.profile.core.services;

import com.flashcards.server.profile.core.dtos.CreateProfileDto;
import com.flashcards.server.profile.core.entites.Profile;
import com.flashcards.server.profile.core.ports.repository.IProfileRepository;
import com.flashcards.server.profile.core.ports.services.ICreateProfile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreateProfile implements ICreateProfile
{
    private final IProfileRepository profileRepository;

    public CreateProfile(IProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Map<String, String> createProfile(CreateProfileDto dto) {
        var profile = new Profile(
                dto.userId(),
                dto.firstName(),
                dto.lastName(),
                dto.avatar(),
                dto.phone(),
                dto.bio()
        );

        var createdProfile = profileRepository.create(profile);

        Map<String, String> result = new HashMap<>();
        result.put("id", createdProfile.getId().toString());
        result.put("createdAt", createdProfile.getCreatedAt().toString());
        result.put("updatedAt", createdProfile.getUpdatedAt().toString());
        result.put("userId", createdProfile.getUserId().toString());
        result.put("firstName", safe(createdProfile.getFirstName()));
        result.put("lastName", safe(createdProfile.getLastName()));
        result.put("avatar", safe(createdProfile.getAvatar()));
        result.put("phone", safe(createdProfile.getPhone()));
        result.put("bio", safe(createdProfile.getBio()));

        return result;
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
