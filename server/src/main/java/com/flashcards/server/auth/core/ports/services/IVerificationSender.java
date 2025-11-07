package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.entities.Account;
import com.flashcards.server.auth.core.entities.User;

public interface IVerificationSender
{
    void send(User user, Account account);
}
