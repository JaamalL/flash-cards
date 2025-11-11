package com.flashcards.server.auth.adapters.http.controllers;

import com.flashcards.server.auth.adapters.http.handlers.UserDetailsHandler;
import com.flashcards.server.common.annotation.Authorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController
{
    private final UserDetailsHandler userDetailsHandler;

    public UserController(UserDetailsHandler userDetailsHandler)
    {
        this.userDetailsHandler = userDetailsHandler;
    }

    @Authorize(allowUnverified = true)
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        return userDetailsHandler.handle(jwt);
    }

}
