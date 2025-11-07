package com.flashcards.server.common.annotation;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Aspect
@Service
public class InternalOnlyAspect
{
    @Value("${auth.internal.internal-secret}")
    private String internalSecret;

    @Around("@annotation(internalOnly)")
    public Object checkInternal(ProceedingJoinPoint pjp, InternalOnly internalOnly) throws Throwable {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            deny("Request context not available");
        }

        var request = (HttpServletRequest) attrs.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null) {
            deny("Request object not found");
        }

        var headerValue = request.getHeader("X-Internal-Token");
        if (headerValue == null || headerValue.isBlank() || !safeEquals(internalSecret, headerValue)) {
            deny("Invalid or missing X-Internal-Token");
        }

        return pjp.proceed();

    }

    private void deny(String message) {
        throw new ApiException(new ApiError(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                message
        ));
    }

    private boolean safeEquals(String a, String b) {
        if (a == null || b == null) return false;
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8)
        );
    }
}

