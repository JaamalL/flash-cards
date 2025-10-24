package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.dtos.RegisterDto;
import com.techpassport.server.auth.core.values.AuthResult;

public interface IRegister {

    AuthResult registerUserByCredentials(RegisterDto dto);
}
