package com.techpassport.server.auth.core.values;

import java.util.UUID;
import java.time.Duration;
import java.time.Instant;

public class BaseToken
{
    private final UUID sub;
    private final long iat;
    private final long exp;

    public BaseToken(UUID sub, Duration lifetime) {
        long now = Instant.now().getEpochSecond();
        this.sub = sub;
        this.iat = now;
        this.exp = now + lifetime.getSeconds();
    }

    public BaseToken(UUID sub, long iat, long exp) {
        this.sub = sub;
        this.iat = iat;
        this.exp = exp;
    }

    public UUID getSub() {
        return sub;
    }
    public long getIat() {
        return iat;
    }
    public long getExp() {
        return exp;
    }
}
