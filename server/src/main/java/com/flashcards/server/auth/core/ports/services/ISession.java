package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.values.AccessAndRefreshTokens;
import com.flashcards.server.auth.core.values.AuthResult;
import com.flashcards.server.common.dtos.ClientInfoDto;

import java.util.UUID;

public interface ISession
{
    AccessAndRefreshTokens createSession(AuthResult authResult, ClientInfoDto clientInfoDto);
    String refreshSession(String refreshToken);
    String refreshSession(UUID userId, UUID accountId, UUID refreshTokenId);
}
