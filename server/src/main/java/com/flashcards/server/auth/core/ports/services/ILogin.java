package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.dtos.LoginDto;
import com.flashcards.server.auth.core.values.AuthResult;

public interface ILogin {
    AuthResult loginUserByCredentials(LoginDto dto);
}
