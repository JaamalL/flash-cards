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
            deny("Request context not available");

        var request = (HttpServletRequest) attrs.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null)
            deny("Request object not found");

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            deny("Missing or invalid Authorization header");

        var token = authHeader.substring(7);

        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException exception) {
            deny("Invalid or expired token");
            return null;
        }

        var requiredRoles = authorize.roles();
        var jwtRole = jwt.getClaimAsString("role");
        if (requiredRoles.length > 0 && !Arrays.asList(requiredRoles).contains(jwtRole))
            deny("User does not have required role");

        var isVerifiedRequired = authorize.isVerified();
        Boolean verified = jwt.getClaim("isVerified");

        if (isVerifiedRequired && (verified == null || !verified))
            deny("User is not verified");

        return pjp.proceed();
    }

    private void deny(String message) {
        throw new ApiException(new ApiError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                message
        ));
    }
}
