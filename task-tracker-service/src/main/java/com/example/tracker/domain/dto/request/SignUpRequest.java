package com.example.tracker.domain.dto.request;

public record SignUpRequest
        (
                String username,
                String email,
                String password
        ) {
}
