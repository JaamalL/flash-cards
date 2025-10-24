package com.techpassport.server.common.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import com.techpassport.server.auth.core.dtos.ClientInfoDto;

public interface IClientInfo
{
    ClientInfoDto getClientInfo(HttpServletRequest request);
}
