package com.techpassport.server.profile.adapters.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController
{
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping(value = "me")
    public ResponseEntity<String> me(
            @AuthenticationPrincipal Jwt jwt
    )
    {
        try {
            var userId = UUID.fromString(jwt.getSubject());
            return ResponseEntity.ok(userId.toString());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("failure");
        }
    }
}
