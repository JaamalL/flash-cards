package com.flashcards.server.common.utils.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class HTTPClient implements IHTTPClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HTTPClient(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> get(String endpoint) {
        try {
            String json = restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(String.class);
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "UNEXPECTED_ERROR",
                    e.getMessage()
            ));
        }
    }

    @Override
    public Map<String, Object> post(String endpoint, Object body) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            String jsonResponse = restClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        } catch (Exception e) {
            throw new ApiException(new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "UNEXPECTED_ERROR",
                    e.getMessage()
            ));
        }
    }

    @Async
    @Override
    public CompletableFuture<Void> postAsync(String endpoint, Object body) {
        return CompletableFuture.runAsync(() -> {
            try {
                String jsonBody = objectMapper.writeValueAsString(body);
                restClient.post()
                        .uri(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody)
                        .retrieve()
                        .body(String.class);
            } catch (Exception e) {
                throw new ApiException(new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "UNEXPECTED_ERROR",
                        e.getMessage()
                ));
            }
        });
    }
}
