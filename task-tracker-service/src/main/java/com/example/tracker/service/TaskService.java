package com.example.tracker.service;

import com.example.tracker.domain.dto.request.TaskFilterDto;
import com.example.tracker.domain.dto.request.TaskRequestDto;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TaskResponseDto;
import com.example.tracker.domain.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponseDto createTask(Long id, TaskRequestDto taskRequest);

    Page<TaskResponseDto> getTasks(Pageable pageable, TaskFilterDto filter);
    TaskResponseDto getTask(Long id);

    TaskResponseDto updateAssignedUser (Long id, UserRequestDto user);
    TaskResponseDto updateStatus(Long id, Status status);
    TaskResponseDto updateTask(Long id, TaskRequestDto task);

    MessageResponse deleteTask(Long id);
}
