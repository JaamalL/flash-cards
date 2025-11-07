package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.values.GooglePayload;

public interface IGoogleTokenExchanger
{
    GooglePayload exchange(String code);
}
