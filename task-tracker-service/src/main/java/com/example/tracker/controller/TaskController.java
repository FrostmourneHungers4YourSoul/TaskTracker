package com.example.tracker.controller;

import com.example.tracker.domain.model.Task;
import com.example.tracker.domain.model.User;
import com.example.tracker.domain.dto.request.TaskFilterDto;
import com.example.tracker.domain.model.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable @NotNull Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PageableDefault Pageable pageable,
                                               @Valid TaskFilterDto filter) {
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable @NotNull Long id,
                                       @RequestBody Task task) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @NotNull Long id) {
        return null;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable @NotNull Long id,
                                             @RequestBody Status status) {
        return null;
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Task> updateAssign(@PathVariable @NotNull Long id,
                                             @RequestBody User user) {
        return null;
    }
}
