package com.example.tracker.domain.dto.response;

import com.example.tracker.domain.model.enums.Role;

public record UserResponseDto
        (
                Long id,
                String username,
                String email,
                String firstName,
                String lastName,
                Role role
        ) {
}
