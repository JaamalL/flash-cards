package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.entities.Account;
import com.techpassport.server.auth.core.entities.User;

public interface IVerificationSender
{
    void send(User user, Account account);
}
