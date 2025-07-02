package com.example.tracker.domain.dto.request;

import com.example.tracker.domain.model.enums.Status;

import java.time.LocalDateTime;

public record TaskFilterDto
        (
                String title,
                Status status,
                String category,
                Long createdBy,
                Long assignedTo,
                Long teamId,
                LocalDateTime deadline,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {
}
