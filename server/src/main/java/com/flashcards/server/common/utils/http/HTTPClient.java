package com.flashcards.server.common.utils.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class HTTPClient implements IHTTPClient {

    @Value("${spring.application.url}")
    private String baseUrl;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HTTPClient(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> get(String endpoint) {
        try {
            var json = restClient.get()
                    .uri(baseUrl + endpoint)
                    .retrieve()
                    .body(String.class);

            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception exception) {
            throw new ApiException(new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "UNEXPECTED_ERROR",
                    exception.getMessage()
            ));
        }
    }

    @Override
    public Map<String, Object> post(String endpoint, Object body) {
        return executePost(endpoint, body, true);
    }

    @Override
    public void postAsync(String endpoint, Object body) {
        Thread.ofVirtual().start(() -> executePost(endpoint, body, false));
    }

    private Map<String, Object> executePost(String endpoint, Object body, boolean waitForResponse) {
        try {
            if (waitForResponse) {
                var jsonBody = objectMapper.writeValueAsString(body);
                var jsonResponse = restClient.post()
                        .uri(baseUrl + endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody)
                        .retrieve()
                        .body(String.class);
                return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception exception) {
            if (waitForResponse) {
                throw new ApiException(new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "UNEXPECTED_ERROR",
                        exception.getMessage()
                ));
            } else {
                System.err.println("Async POST request failed: " + exception.getMessage());
                return Collections.emptyMap();
            }
        }
    }
}
