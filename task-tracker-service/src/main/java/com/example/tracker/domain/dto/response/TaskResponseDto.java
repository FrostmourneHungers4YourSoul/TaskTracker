package com.example.tracker.domain.dto.response;

import com.example.tracker.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TaskResponseDto
        (
                Long id,
                String title,
                String description,
                Status status,
                Integer priority,
                String category,
                @JsonProperty("created_by") UserResponseDto createdBy,
                @JsonProperty("assigned_to") UserResponseDto assignedTo,
                TeamResponseDto team,
                LocalDateTime deadline,
                @JsonProperty("created_at") LocalDateTime createdAt,
                @JsonProperty("updated_at") LocalDateTime updatedAt
        ) {


}
