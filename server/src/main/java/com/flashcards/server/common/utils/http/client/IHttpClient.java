package com.flashcards.server.common.utils.http.client;

import org.springframework.http.HttpHeaders;

public interface IHttpClient {

    <T> T get(String endpoint, HttpHeaders headers, Class<T> responseType);

    <T, B> T post(String endpoint, B body, HttpHeaders headers, Class<T> responseType);
    <B> void postAsync(String endpoint, B body, HttpHeaders headers);
}