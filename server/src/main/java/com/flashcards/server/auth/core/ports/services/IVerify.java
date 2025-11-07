package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.values.AuthResult;

import java.util.UUID;

public interface IVerify
{
    AuthResult verifyUserByToken(String token);
    AuthResult verifyUserByCode(UUID userId, UUID accountId, String code);
}
