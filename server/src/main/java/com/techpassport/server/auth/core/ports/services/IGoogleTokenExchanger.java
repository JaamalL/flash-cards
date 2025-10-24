package com.techpassport.server.auth.core.ports.services;

import com.techpassport.server.auth.core.values.GooglePayload;

public interface IGoogleTokenExchanger
{
    GooglePayload exchange(String code);
}
