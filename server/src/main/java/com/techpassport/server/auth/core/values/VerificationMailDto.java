package com.techpassport.server.auth.core.values;

import java.time.Duration;

public record VerificationMailDto (
    String email,
    String code,
    String token,
    Duration ttl
) {
    public long lifetimeMinutes() {
        return ttl.toMinutes();
    }
}
