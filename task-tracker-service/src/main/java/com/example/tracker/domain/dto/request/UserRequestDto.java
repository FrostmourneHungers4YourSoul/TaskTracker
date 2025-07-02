package com.example.tracker.domain.dto.request;

import com.example.tracker.domain.model.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto
        (
                @NotBlank String username,
                String email,
                String password,
                String firstName,
                String lastName,
                Role role
        ) {
}
