package com.flashcards.server.common.utils.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HTTPConfiguration
{
    @Value("${auth.internal.internal-secret}")
    private String internalSecret;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("X-Internal-Token", internalSecret)
                .build();
    }
}
