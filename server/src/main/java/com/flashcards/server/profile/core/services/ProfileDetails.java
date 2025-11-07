package com.flashcards.server.profile.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.profile.core.entites.Profile;
import com.flashcards.server.profile.core.ports.repository.IProfileRepository;
import com.flashcards.server.profile.core.ports.services.IProfileDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProfileDetails implements IProfileDetails
{
    private final IProfileRepository profileRepository;

    public ProfileDetails(IProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Map<String, String> getProfileDetails(UUID userId)
    {
        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.NOT_FOUND,
                        "PROFILE_NOT_FOUND",
                        "Profile with this userId was not found"
                )));

        return buildResult(profile);
    }

    private Map<String, String> buildResult(Profile profile) {
        Map<String, String> result = new HashMap<>();
        result.put("id", profile.getId().toString());
        result.put("createdAt", profile.getCreatedAt().toString());
        result.put("updatedAt", profile.getUpdatedAt().toString());
        result.put("userId", profile.getUserId().toString());
        result.put("firstName", safe(profile.getFirstName()));
        result.put("lastName", safe(profile.getLastName()));
        result.put("avatar", safe(profile.getAvatar()));
        result.put("phone", safe(profile.getPhone()));
        result.put("bio", safe(profile.getBio()));

        return result;
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
