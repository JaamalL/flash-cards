package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.entities.User;
import com.techpassport.server.auth.core.values.AuthResult;

import java.util.UUID;

public interface IVerification
{
    AuthResult verifyUserByToken(String token);
    AuthResult verifyUserByCode(UUID userId, UUID accountId, String code);
}
