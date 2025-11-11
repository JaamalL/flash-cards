package com.flashcards.server.common.utils.http.request;

import com.flashcards.server.common.dtos.ClientInfoDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IHttpRequestInfo
{
    ClientInfoDto getClientInfo(HttpServletRequest request);
}
