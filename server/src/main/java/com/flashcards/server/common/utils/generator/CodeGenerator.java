package com.flashcards.server.common.utils.generator;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeGenerator implements ICodeGenerator
{
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generateCode(int length, boolean onlyNumbers)
    {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        var characters = onlyNumbers ? NUMBERS : NUMBERS + LETTERS;
        var code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            var index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}
