package com.techpassport.server.common.utils.hasher;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import org.springframework.stereotype.Component;

@Component
public class PasswordHasher implements IPasswordHasher
{
    private static final int ITERATIONS = 5;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 2;

    private final Argon2 argon2 = Argon2Factory.create();

    @Override
    public String hashPassword(String password)
    {
        try
        {
            return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
        }
        finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

    @Override
    public boolean verifyPassword(String hash, String password)
    {
        return argon2.verify(hash, password.toCharArray());
    }
}