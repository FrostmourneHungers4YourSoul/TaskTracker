package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.UserResponseDto;
import com.example.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "API Users", description = "Управление пользователями")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получить список пользователей",
            description = "Доступ только ADMIN и MANAGER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно получен список пользователей")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Доступно всем авторизованным пользователям"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет данные пользователя по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Новые данные пользователя", required = true)
            @RequestBody @Valid UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.updateUser(id, requestDto));
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по ID. Доступ только ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
