package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.values.AccessToken;
import com.techpassport.server.auth.core.values.RefreshToken;
import com.techpassport.server.auth.core.values.VerifyToken;

public interface ITokenSerializer
{
    String SerializeAccessToken(AccessToken token);
    String SerializeRefreshToken(RefreshToken token);
    String SerializeVerifyToken(VerifyToken token);

    AccessToken DeserializeAccessToken(String token);
    RefreshToken DeserializeRefreshToken(String token);
    VerifyToken DeserializeVerifyToken(String token);
}
