package com.flashcards.server.auth.adapters.http.handlers;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieFactory {

    @Value("${auth.tokens.access-token.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${auth.tokens.refresh-token.lifetime}")
    private Duration refreshTokenLifetime;

    public Cookie refreshToken(String token) {
        var cookie = new Cookie("refreshToken", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) refreshTokenLifetime.toSeconds());
        return cookie;
    }

    public Cookie deleteAccessToken() {
        var cookie = new Cookie("accessToken", "");
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}