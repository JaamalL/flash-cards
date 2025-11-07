package com.flashcards.server.common.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import com.flashcards.server.auth.core.dtos.ClientInfoDto;

public interface IHttpClientDetails
{
    ClientInfoDto getClientInfo(HttpServletRequest request);
}
