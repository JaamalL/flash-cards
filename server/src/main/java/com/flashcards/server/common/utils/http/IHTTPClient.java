package com.flashcards.server.common.utils.http;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IHTTPClient
{
    Map<String, Object> get(String endpoint);
    Map<String, Object> post(String endpoint, Object body);
    CompletableFuture<Void> postAsync(String endpoint, Object body);
}
