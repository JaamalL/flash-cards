package com.flashcards.server.profile.adapters.http.handlers;

import com.flashcards.server.profile.core.dtos.CreateProfileDto;
import com.flashcards.server.profile.core.ports.services.ICreateProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateProfileHandler
{
    private final ICreateProfile createProfile;

    public CreateProfileHandler(ICreateProfile createProfile)
    {
        this.createProfile = createProfile;
    }

    public ResponseEntity<Map<String, String>> handle(CreateProfileDto dto)
    {
        var result = createProfile.createProfile(dto);
        return ResponseEntity.ok(result);
    }

}
