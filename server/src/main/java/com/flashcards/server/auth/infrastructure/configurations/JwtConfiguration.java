package com.flashcards.server.auth.infrastructure.configurations;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.nio.charset.StandardCharsets;


@Configuration
public class JwtConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("classpath:keys/pkcs8_private.json")
    private Resource privateJwkResource;

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        var jwkJson = new String(privateJwkResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        var jwkSet = new JWKSet(JWK.parse(jwkJson));
        var jwkSource = new ImmutableJWKSet<>(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        System.out.println(jwkSetUri);

        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}