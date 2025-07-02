package com.example.tracker.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TeamResponseDto
        (
                Long id,
                String name,
                String description,
                @JsonProperty("created_by") UserResponseDto createdBy,
                @JsonProperty("created_at") LocalDateTime createdAt,
                @JsonProperty("updated_at") LocalDateTime updatedAt
        ) {
}
