package com.flashcards.server.profile.adapters.http.handlers;

import com.flashcards.server.profile.core.ports.services.IProfileDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ProfileDetailsHandler {
    private final IProfileDetails profileDetails;

    public ProfileDetailsHandler(IProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ResponseEntity<Map<String, String>> handle(Jwt jwt)
    {
        var userId = UUID.fromString(jwt.getSubject());

        var result = profileDetails.getProfileDetails(userId);
        return ResponseEntity.ok(result);
    }
}
