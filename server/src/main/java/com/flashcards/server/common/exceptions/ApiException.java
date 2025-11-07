package com.flashcards.server.common.exceptions;

import com.flashcards.server.common.error.ApiError;

public class ApiException extends RuntimeException {
    private final ApiError error;

    public ApiException(ApiError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}