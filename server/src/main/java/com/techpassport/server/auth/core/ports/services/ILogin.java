package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.dtos.LoginDto;
import com.techpassport.server.auth.core.values.AuthResult;

public interface ILogin {
    AuthResult loginUserByCredentials(LoginDto dto);
}
