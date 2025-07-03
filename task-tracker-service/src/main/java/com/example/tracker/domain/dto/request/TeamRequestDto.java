package com.example.tracker.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto
        (
                @NotBlank String name,
                String description
        ) {
}
