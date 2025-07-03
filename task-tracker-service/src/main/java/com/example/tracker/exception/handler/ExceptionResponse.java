package com.example.tracker.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse
        (
                @JsonProperty("status_code") int statusCode,
                String error,
                String message
        ) {
}
