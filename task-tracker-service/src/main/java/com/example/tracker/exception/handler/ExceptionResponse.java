package com.example.tracker.exception.handler;

public record ExceptionResponse
        (
                int statusCode,
                String error,
                String message
        ) {
}
