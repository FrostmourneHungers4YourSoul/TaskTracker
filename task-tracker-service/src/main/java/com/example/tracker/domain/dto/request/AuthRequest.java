package com.example.tracker.domain.dto.request;

public record AuthRequest
        (
                String username,
                String password
        ) {
}

