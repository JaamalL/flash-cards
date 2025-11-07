package com.flashcards.server.auth.core.ports.services;

import com.flashcards.server.auth.core.dtos.CreateProfileDto;

import java.util.Map;

public interface IProfileCreateClient
{
    Map<String, Object> createProfile(CreateProfileDto dto);
}
