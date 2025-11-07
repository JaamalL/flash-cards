package com.flashcards.server.common.utils.hasher;

public interface IPasswordHasher
{
    String hashPassword(String password);
    boolean verifyPassword(String password, String hashedPassword);
}
