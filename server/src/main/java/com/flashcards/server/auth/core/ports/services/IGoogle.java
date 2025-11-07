package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.values.AuthResult;

public interface IGoogle
{
    AuthResult googleAuth(String code);
}
