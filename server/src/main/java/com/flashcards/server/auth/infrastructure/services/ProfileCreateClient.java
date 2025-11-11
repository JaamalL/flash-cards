package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.dtos.CreateProfileDto;
import com.flashcards.server.auth.core.ports.services.IProfileCreateClient;
import com.flashcards.server.common.utils.http.client.IHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Service
public class ProfileCreateClient implements IProfileCreateClient {

    private static final Logger logger = LoggerFactory.getLogger(ProfileCreateClient.class);

    @Value("${spring.application.url}")
    private String baseUrl;

    private final IHttpClient httpClient;

    public ProfileCreateClient(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, Object> createProfile(CreateProfileDto dto) {
        var profileDto = new CreateProfileDto(dto.userId(), dto.firstName(),  dto.lastName(), dto.avatar(), dto.phone(), dto.bio());
        var url = String.format("%s/profile/create", baseUrl);

        return httpClient.post(url, profileDto, null, Map.class);


    }
}
