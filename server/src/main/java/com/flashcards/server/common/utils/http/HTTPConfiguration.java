//package com.flashcards.server.common.utils.http;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class HTTPConfiguration {
//
//    @Value("${auth.internal.internal-secret}")
//    private String internalSecret;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate template = new RestTemplate();
//        template.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().add("X-Internal-Token", internalSecret);
//            return execution.execute(request, body);
//        });
//        return template;
//    }
//}

package com.flashcards.server.common.utils.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HTTPConfiguration
{

    @Value("${spring.application.url}")
    private String baseUrl;

    @Value("${auth.internal.internal-secret}")
    private String internalSecret;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Internal-Token", internalSecret)
                .build();
    }
}
