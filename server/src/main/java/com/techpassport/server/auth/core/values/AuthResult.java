package com.techpassport.server.auth.core.values;

import java.util.UUID;

import com.techpassport.server.auth.core.entities.User;

public record AuthResult(User user, UUID accountId) {}