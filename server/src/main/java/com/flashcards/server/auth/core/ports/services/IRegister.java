package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.dtos.RegisterDto;
import com.flashcards.server.auth.core.values.AuthResult;

public interface IRegister {

    AuthResult registerUserByCredentials(RegisterDto dto);
}
