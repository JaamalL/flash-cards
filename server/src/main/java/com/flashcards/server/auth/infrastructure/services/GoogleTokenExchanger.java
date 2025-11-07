package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.ports.services.IGoogleTokenExchanger;
import com.flashcards.server.auth.core.values.GooglePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Base64;
import java.util.Map;

@Service
public class GoogleTokenExchanger implements IGoogleTokenExchanger {

    @Value("${auth.oauth.google.client-id}")
    private String clientId;

    @Value("${auth.oauth.google.client-secret}")
    private String clientSecret;

    @Value("${auth.oauth.google.redirect-uri}")
    private String redirectUri;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GoogleTokenExchanger(RestClient restClient, ObjectMapper objectMapper) {
       this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public GooglePayload exchange(String code) {
        var body = requestToken(code);
        var idToken = body.get("id_token").toString();
        var payloadMap = parseIdToken(idToken);

        return new GooglePayload(
                payloadMap.get("sub").toString(),
                payloadMap.get("email").toString(),
                Boolean.parseBoolean(payloadMap.getOrDefault("email_verified", false).toString()),
                payloadMap.getOrDefault("given_name", "").toString(),
                payloadMap.getOrDefault("family_name", "").toString(),
                payloadMap.getOrDefault("picture", "").toString()
        );
    }

    private Map<String, Object> requestToken(String code) {
        try {
            String json = restClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body("code=" + code
                            + "&client_id=" + clientId
                            + "&client_secret=" + clientSecret
                            + "&redirect_uri=" + redirectUri
                            + "&grant_type=authorization_code")
                    .retrieve()
                    .body(String.class);

            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get ID token from Google", exception);
        }
    }

    private Map<String, Object> parseIdToken(String idToken) {
        var parts = idToken.split("\\.");
        if (parts.length != 3)
            throw new IllegalStateException("Invalid ID token format");

        try {
            var payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return objectMapper.readValue(payloadJson, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse ID token payload", e);
        }
    }
}
