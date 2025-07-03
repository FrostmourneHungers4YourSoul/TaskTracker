package com.example.tracker.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    NEW,
    IN_PROGRESS,
    REVIEW,
    COMPLETED,
    CANCELED;

    @JsonCreator
    public static Role from(String value) {
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role: " + value);
        }
    }
}
