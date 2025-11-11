package com.flashcards.server.common.annotation;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.HashSet;

@Aspect
@Service
public class AuthorizeAspect
{
    private final JwtDecoder jwtDecoder;

    public AuthorizeAspect(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Around("@annotation(authorize)")
    public Object checkAuthorize(ProceedingJoinPoint pjp, Authorize authorize) throws Throwable {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null)
            deny(HttpStatus.INTERNAL_SERVER_ERROR, "Request context not available");

        var request = (HttpServletRequest) attrs.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null)
            deny(HttpStatus.INTERNAL_SERVER_ERROR, "Request object not found");

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            deny(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");

        var token = authHeader.substring(7);

        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException exception) {
            deny(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
            return null;
        }

        var jwtRoles = jwt.getClaimAsStringList("roles");
        if (jwtRoles == null || jwtRoles.isEmpty())
            deny(HttpStatus.FORBIDDEN, "User has no roles");

        var userRoles = new HashSet<>(jwtRoles);
        var requiredRoles = new HashSet<>(Arrays.asList(authorize.roles()));

        var hasRole = userRoles.stream().anyMatch(requiredRoles::contains);
        if (!hasRole)
            deny(HttpStatus.FORBIDDEN, "User does not have required role");

        var allowUnverified = authorize.allowUnverified();
        Boolean verified = jwt.getClaim("isVerified");

        if (!allowUnverified && (verified == null || !verified))
            deny(HttpStatus.FORBIDDEN, "User is not verified");

        return pjp.proceed();
    }

    private void deny(HttpStatus status, String message) {
        throw new ApiException(new ApiError(
                status,
                "UNAUTHORIZED",
                message
        ));
    }
}
