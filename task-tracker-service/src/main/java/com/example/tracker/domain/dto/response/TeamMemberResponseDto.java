package com.example.tracker.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TeamMemberResponseDto
        (
                Long id,
                @JsonProperty("team_id") TeamResponseDto team,
                @JsonProperty("user_id") UserResponseDto user,
                @JsonProperty("joined_at") LocalDateTime joinedAt
        ) {
}
