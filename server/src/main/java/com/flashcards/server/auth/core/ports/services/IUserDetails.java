package com.flashcards.server.auth.core.ports.services;

import java.util.Map;
import java.util.UUID;

public interface IUserDetails
{
    Map<String, Object> getUserDetails(UUID userId, UUID accountId);
}
