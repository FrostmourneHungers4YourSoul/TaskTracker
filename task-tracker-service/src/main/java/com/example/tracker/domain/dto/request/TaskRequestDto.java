package com.example.tracker.domain.dto.request;

import com.example.tracker.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TaskRequestDto
        (
                @NotBlank String title,
                String description,
                Status status,
                Integer priority,
                String category,
                @JsonProperty("assigned_to_id") Long assignedTo,
                @JsonProperty("team_id") Long teamId,
                LocalDateTime deadline
        ) {
}
