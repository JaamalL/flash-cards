package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.dtos.ClientInfoDto;
import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.values.AccessAndRefreshTokens;
import com.techpassport.server.auth.core.values.AuthResult;

import java.util.UUID;

public interface ISession
{
    AccessAndRefreshTokens createSession(AuthResult authResult, ClientInfoDto clientInfoDto);
    String refreshSession(String refreshToken);
    String refreshSession(UUID userId, UUID accountId);
}
