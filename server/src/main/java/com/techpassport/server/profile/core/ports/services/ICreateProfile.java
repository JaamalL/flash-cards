package com.techpassport.server.profile.core.ports.services;

import com.techpassport.server.profile.core.dtos.CreateProfileDto;
import com.techpassport.server.profile.core.entites.Profile;

public interface ICreateProfile
{
    Profile createProfile(CreateProfileDto dto);
}
