package com.techpassport.server.auth.core.values;

import java.time.Duration;
import java.util.UUID;

public class RefreshToken extends BaseToken
{
    private final UUID id;
    private final UUID accountId;

    private final String os;
    private final String device;
    private final String ip;
    private final String browser;

    public RefreshToken(
          UUID id,
          UUID userId,
          UUID accountId,
          Duration lifetime,
          String os,
          String device,
          String ip,
          String browser
    ){
        super(userId, lifetime);
        this.id = id;
        this.accountId = accountId;
        this.os = os;
        this.device = device;
        this.ip = ip;
        this.browser = browser;
    }

    public RefreshToken(
            UUID id,
            UUID userId,
            UUID accountId,
            String os,
            String device,
            String ip,
            String browser,
            long iat,
            long exp
    ){
        super(userId, iat, exp);
        this.id = id;
        this.accountId = accountId;
        this.os = os;
        this.device = device;
        this.ip = ip;
        this.browser = browser;
    }

    public UUID getId()
    {
        return id;
    }
    public UUID getAccountId()
    {
        return accountId;
    }
    public String getOs()
    {
        return os;
    }
    public String getDevice()
    {
        return device;
    }
    public String getIp()
    {
        return ip;
    }
    public String getBrowser()
    {
        return browser;
    }
}
