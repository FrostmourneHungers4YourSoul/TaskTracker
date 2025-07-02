package com.example.tracker.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamMemberRequestDto
        (
                @JsonProperty("user_id") Long userId
        ) {
}
