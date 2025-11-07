package com.flashcards.server.auth.adapters.http.handlers;

import com.flashcards.server.auth.core.ports.services.IUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class UserDetailsHandler
{
    private final IUserDetails userDetails;

    public UserDetailsHandler(IUserDetails userDetails)
    {
        this.userDetails = userDetails;
    }

    public ResponseEntity<Map<String, Object>> handle(Jwt jwt) {
        var userId = UUID.fromString(jwt.getSubject());
        var accountId = UUID.fromString(jwt.getClaimAsString("accountId"));

        var result = userDetails.getUserDetails(userId, accountId);
        return ResponseEntity.ok(result);
    }
}
