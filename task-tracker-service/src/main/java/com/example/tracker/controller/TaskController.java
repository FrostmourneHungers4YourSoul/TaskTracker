package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.TaskFilterDto;
import com.example.tracker.domain.dto.request.TaskRequestDto;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TaskResponseDto;
import com.example.tracker.domain.model.enums.Status;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@RequestBody @Valid TaskRequestDto taskRequest,
                                                  @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(user.getId(), taskRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getTasks(@PageableDefault Pageable pageable,
                                                          @ModelAttribute @Validated TaskFilterDto filter) {
        return ResponseEntity.ok(taskService.getTasks(pageable, filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable @NotNull Long id,
                                                  @RequestBody TaskRequestDto task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable @NotNull Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(taskService.deleteTask(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(@PathVariable @NotNull Long id,
                                                        @RequestBody Status status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDto> updateAssign(@PathVariable @NotNull Long id,
                                             @RequestBody UserRequestDto user) {
        return ResponseEntity.ok(taskService.updateAssignedUser(id, user));
    }
}
