package com.flashcards.server.profile.core.ports.services;

import java.util.Map;
import java.util.UUID;

public interface IProfileDetails
{
    Map<String, String> getProfileDetails(UUID userId);
}
