package com.techpassport.server.auth.core.dtos;

import jakarta.validation.constraints.Email;

public record RegisterDto
(
    @Email
    String email,

    String password
) {}