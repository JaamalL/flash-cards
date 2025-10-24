package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.values.AuthResult;

public interface IGoogle
{
    AuthResult googleAuth(String code);
}
