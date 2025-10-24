package com.techpassport.server.auth.core.ports.services;

public interface ILogout
{
    void deleteSession(String refreshToken);
}
