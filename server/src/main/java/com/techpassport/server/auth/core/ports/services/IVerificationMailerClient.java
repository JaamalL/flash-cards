package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.values.VerificationMailDto;

public interface IVerificationMailerClient
{
    void sendVerificationMail(VerificationMailDto dto);
}
