package com.example.tracker.domain.dto.request;

import com.example.tracker.domain.model.enums.Status;

public record TaskFilterDto
        (
                Status status,
                String category,
                Long teamId
        ) {
}
