package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.dtos.CreateProfileDto;
import com.flashcards.server.auth.core.ports.services.IProfileCreateClient;
import com.flashcards.server.common.utils.http.IHTTPClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileCreateClient implements IProfileCreateClient
{
    private final IHTTPClient httpClient;

    public ProfileCreateClient(IHTTPClient httpClient)
    {
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, Object> createProfile(CreateProfileDto dto)
    {
        Map<String, Object> profileDto = new HashMap<>();
        profileDto.put("userId", dto.userId().toString());
        profileDto.put("firstName", dto.firstName());
        profileDto.put("lastName", dto.lastName());
        profileDto.put("avatar", dto.avatar());
        profileDto.put("phone", dto.phone());
        profileDto.put("bio", dto.bio());

        return httpClient.post("/profile/create", profileDto);
    }
}
