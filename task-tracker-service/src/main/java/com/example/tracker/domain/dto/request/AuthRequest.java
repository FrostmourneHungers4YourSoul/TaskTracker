package com.example.tracker.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель запроса на аутентификацию")
public record AuthRequest
        (
                @Schema(description = "Пользователь", example = "user123")
                String username,
                @Schema(description = "Пароль", example = "password123")
                String password
        ) {
}

