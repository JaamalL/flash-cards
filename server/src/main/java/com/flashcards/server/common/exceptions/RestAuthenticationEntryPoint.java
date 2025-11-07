package com.flashcards.server.common.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashcards.server.common.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        var apiError = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                authException.getMessage() != null ? authException.getMessage() : "Authentication required"
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of(
                        "code", apiError.getCode(),
                        "message", apiError.getMessage()
                )
        ));
    }
}