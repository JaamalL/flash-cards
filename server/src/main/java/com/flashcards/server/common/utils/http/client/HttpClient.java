package com.flashcards.server.common.utils.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.mail.adapters.http.MailController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Map;

@Service
public class HttpClient implements IHttpClient
{
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    @Value("${auth.internal.internal-secret}")
    private String internalSecret;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HttpClient() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        this.restClient = RestClient.builder()
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            try {
                                ApiError apiError = objectMapper.readValue(response.getBody(), ApiError.class);
                                throw new ApiException(apiError);
                            } catch (IOException | RuntimeException exception) {
                                ApiError fallbackError = new ApiError(
                                        (HttpStatus) response.getStatusCode(),
                                        ((HttpStatus) response.getStatusCode()).name(),
                                        response.getStatusText()
                                );
                                throw new ApiException(fallbackError);
                            }
                        }
                )
                .build();
    }

    private HttpHeaders prepareHeaders(HttpHeaders headers) {
        HttpHeaders result = new HttpHeaders();
        result.add("X-Internal-Token", internalSecret);
        if (headers != null) result.addAll(headers);
        return result;
    }

    @Override
    public <T> T get(String endpoint, HttpHeaders headers, Class<T> responseType) {
        return restClient.get()
                .uri(endpoint)
                .headers(h -> h.addAll(prepareHeaders(headers)))
                .retrieve()
                .body(responseType);
    }

    @Override
    public <T, B> T post(String endpoint, B body, HttpHeaders headers, Class<T> responseType) {
        return restClient.post()
                .uri(endpoint)
                .headers(h -> h.addAll(prepareHeaders(headers)))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    @Override
    public <B> void postAsync(String endpoint, B body, HttpHeaders headers) {
        Thread.startVirtualThread(() -> {
            try {
                restClient.post()
                        .uri(endpoint)
                        .headers(h -> h.addAll(prepareHeaders(headers)))
                        .body(body)
                        .retrieve()
                        .body(Map.class);
            } catch (Exception e) {
                log.error("Async POST failed to {}: {}", endpoint, e.getMessage());
            }
        });
    }
}
