package com.example.tracker.domain.dto.response;

public record AuthResponse
        (
                String accessToken,
                String refreshToken
        ) {
}
