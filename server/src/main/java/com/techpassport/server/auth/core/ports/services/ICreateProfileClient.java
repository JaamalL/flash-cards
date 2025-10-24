package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.dtos.CreateProfileDto;

import java.util.UUID;

public interface ICreateProfileClient
{
    UUID createProfile(CreateProfileDto dto);
}
