package com.example.tracker.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto
        (
                @NotBlank String name,
                String description,
                @JsonProperty("creator_id") Long creatorId
        ) {
}
