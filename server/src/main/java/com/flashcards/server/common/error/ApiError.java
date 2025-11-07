package com.flashcards.server.common.error;

import org.springframework.http.HttpStatus;

public class ApiError
{
    private final HttpStatus status;
    private final String code;
    private final String message;

    public ApiError(HttpStatus status, String code, String message)
    {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus()
    {
        return status;
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"status\":" + status + "," +
                "\"code\":\"" + code + "\"," +
                "\"message\":\"" + message + "\"" +
                "}";
    }
}
