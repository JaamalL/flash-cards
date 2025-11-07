package com.flashcards.server.common.utils.http;

import java.util.Map;

public interface IHTTPClient
{
    Map<String, Object> get(String endpoint);
    Map<String, Object> post(String endpoint, Object body);
    void postAsync(String endpoint, Object body);
}
