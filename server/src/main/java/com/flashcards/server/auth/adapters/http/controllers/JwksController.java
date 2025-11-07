package com.flashcards.server.auth.adapters.http.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(value = "/.well-known")
public class JwksController {
    @Value("classpath:.well-known/jwks.json")
    private Resource publicJwkResource;

    @GetMapping(value = "jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJwksJson() {
        try (var is = publicJwkResource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new UncheckedIOException("Cannot read JWKS", ex);
        }
    }
}
