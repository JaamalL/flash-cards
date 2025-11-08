package com.flashcards.server.auth.infrastructure.services;

import com.flashcards.server.auth.core.ports.services.IGoogleTokenExchanger;
import com.flashcards.server.auth.core.values.GooglePayload;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcards.server.common.utils.http.IHTTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    private final IHTTPClient httpClient;
    private final ObjectMapper objectMapper;

    public GoogleTokenExchanger(IHTTPClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public GooglePayload exchange(String code) {
        Map<String, Object> body = requestToken(code);
        String idToken = body.get("id_token").toString();
        Map<String, Object> payloadMap = parseIdToken(idToken);

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
            String bodyForm = "code=" + code
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + redirectUri
                    + "&grant_type=authorization_code";

            return httpClient.post(
                    "https://oauth2.googleapis.com/token",
                    bodyForm
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get ID token from Google", e);
        }
    }

    private Map<String, Object> parseIdToken(String idToken) {
        String[] parts = idToken.split("\\.");
        if (parts.length != 3) {
            throw new IllegalStateException("Invalid ID token format");
        }
        try {
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return objectMapper.readValue(payloadJson, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse ID token payload", e);
        }
    }
}
