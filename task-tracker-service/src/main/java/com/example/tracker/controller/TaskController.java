package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.TaskFilterDto;
import com.example.tracker.domain.dto.request.TaskRequestDto;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TaskResponseDto;
import com.example.tracker.domain.model.enums.Status;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "API Tasks", description = "Управление задачами")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Создать задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(
            @Parameter(description = "Данные задачи", required = true)
            @RequestBody @Valid TaskRequestDto taskRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(user.getId(), taskRequest));
    }

    @Operation(summary = "Получить задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @Operation(summary = "Получить список задач с пагинацией и фильтрацией")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getTasks(
            @ParameterObject @PageableDefault(size = 3) Pageable pageable,
            @ParameterObject @ModelAttribute @Validated TaskFilterDto filter) {
        return ResponseEntity.ok(taskService.getTasks(pageable, filter));
    }

    @Operation(summary = "Обновить задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Обновлённые данные задачи", required = true)
            @RequestBody TaskRequestDto task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @Operation(summary = "Удалить задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable @NotNull Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(taskService.deleteTask(id));
    }

    @Operation(summary = "Изменить статус задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус задачи обновлён"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Новый статус", required = true)
            @RequestBody Status status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @Operation(summary = "Назначить пользователя на задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь назначен"),
            @ApiResponse(responseCode = "404", description = "Задача или пользователь не найдены")
    })
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDto> updateAssign(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Данные пользователя", required = true)
            @RequestBody UserRequestDto user) {
        return ResponseEntity.ok(taskService.updateAssignedUser(id, user));
    }
}
