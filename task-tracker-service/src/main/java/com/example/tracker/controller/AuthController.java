package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.AuthRequest;
import com.example.tracker.domain.dto.request.RefreshTokenRequest;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.AuthResponse;
import com.example.tracker.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "API Authentication", description = "Авторизация с использованием JWT и Refresh токена")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная регистрация и возврат токена"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserRequestDto request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Аутентификация пользователя (логин)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход и возврат токенов"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Данные для входа", required = true)
            @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Выход из системы (удаление refresh токена)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Успешный выход"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "Refresh токен", required = true)
            @RequestBody RefreshTokenRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить токен по refresh токену")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новые access и refresh токены выданы"),
            @ApiResponse(responseCode = "403", description = "Недействительный или просроченный refresh токен")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Parameter(description = "Refresh токен", required = true)
            @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
