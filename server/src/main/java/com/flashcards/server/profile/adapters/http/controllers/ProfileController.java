package com.flashcards.server.profile.adapters.http.controllers;

import com.flashcards.server.common.annotation.Authorize;
import com.flashcards.server.common.annotation.InternalOnly;
import com.flashcards.server.profile.adapters.http.handlers.CreateProfileHandler;
import com.flashcards.server.profile.adapters.http.handlers.ProfileDetailsHandler;
import com.flashcards.server.profile.core.dtos.CreateProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController
{
    private final CreateProfileHandler createProfileHandler;
    private final ProfileDetailsHandler profileDetailsHandler;

    public ProfileController(
            CreateProfileHandler createProfileHandler,
            ProfileDetailsHandler profileDetailsHandler
    ) {
        this.createProfileHandler = createProfileHandler;
        this.profileDetailsHandler = profileDetailsHandler;
    }

    @InternalOnly
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody CreateProfileDto dto) {
        return createProfileHandler.handle(dto);
    }

    @Authorize(isVerified = false)
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(@AuthenticationPrincipal Jwt jwt) {
        return profileDetailsHandler.handle(jwt);
    }


}