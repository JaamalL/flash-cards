package com.flashcards.server.profile.core.ports.services;

import com.flashcards.server.profile.core.dtos.CreateProfileDto;

import java.util.Map;

public interface ICreateProfile
{
    Map<String, String> createProfile(CreateProfileDto dto);
}
