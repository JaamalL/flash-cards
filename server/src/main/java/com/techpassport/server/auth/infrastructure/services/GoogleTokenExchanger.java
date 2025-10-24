package com.techpassport.server.auth.infrastructure.services;

import com.techpassport.server.auth.core.ports.services.IGoogleTokenExchanger;
import com.techpassport.server.auth.core.values.GooglePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleTokenExchanger(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = new HttpEntity<>(params, headers);
        var tokenUrl = "https://oauth2.googleapis.com/token";

        var response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        var body = response.getBody();
        if (body == null || !body.containsKey("id_token")) {
            throw new IllegalStateException("Failed to get ID token from Google");
        }
        return body;
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
