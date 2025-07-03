package com.example.tracker.service.impl;

import com.example.tracker.domain.dto.request.TaskFilterDto;
import com.example.tracker.domain.dto.request.TaskRequestDto;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TaskResponseDto;
import com.example.tracker.domain.model.Task;
import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.User;
import com.example.tracker.domain.model.enums.Status;
import com.example.tracker.exception.ResourceNotFoundException;
import com.example.tracker.mapper.TaskMapper;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.service.TaskService;
import com.example.tracker.service.TeamService;
import com.example.tracker.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final UserService userService;
    private final TeamService teamService;
    private final TaskMapper mapper;

    @Override
    @Transactional
    public TaskResponseDto createTask(Long userId, TaskRequestDto taskRequest) {
        User creator = userService.getUserById(userId);
        User user = userService.getUserById(taskRequest.assignedTo());
        Team team = teamService.getTeamById(taskRequest.teamId());
        Task task = mapper.toEntity(taskRequest);

        task.setCreatedBy(creator);
        task.setTeam(team);
        task.setAssignedTo(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        task = repository.save(task);
        log.info("Task has been created: {}", task);
        return mapper.toDto(task);
    }

    @Override
    public Page<TaskResponseDto> getTasks(Pageable pageable, TaskFilterDto filter) {
        Specification<Task> spec = buildSpecificationFromFilter(filter);
        Page<Task> page = repository.findAll(spec, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    public TaskResponseDto getTask(Long id) {
        Task task = getTaskById(id);
        log.info("Found task: {}", task);
        return mapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskResponseDto updateAssignedUser(Long id, UserRequestDto user) {
        User assignedUser = userService.getUserByUsername(user.username());
        Task task = getTaskById(id);

        task.setAssignedTo(assignedUser);
        task.setUpdatedAt(LocalDateTime.now());

        task = repository.save(task);

        log.info("[{}] assigned to updated: [{}]", task.getTitle(), task);
        return mapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskResponseDto updateStatus(Long id, Status status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());

        task = repository.save(task);

        log.info("[{}] status updated: [{}]", task.getTitle(), task);
        return null;
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto task) {
        Task updatingTask = getTaskById(id);
        User user = userService.getUserById(task.assignedTo());
        Team team = teamService.getTeamById(task.teamId());

        Task updatedTask = mapper.toEntity(task);

        updatedTask.setId(updatingTask.getId());
        updatedTask.setCreatedBy(updatingTask.getCreatedBy());
        updatedTask.setAssignedTo(user);
        updatedTask.setTeam(team);
        updatedTask.setCreatedAt(updatingTask.getCreatedAt());
        updatedTask.setUpdatedAt(LocalDateTime.now());

        updatedTask = repository.save(updatedTask);

        log.info("Task [{}] has been updated.", updatedTask.getTitle());
        return mapper.toDto(updatingTask);
    }

    @Override
    @Transactional
    public MessageResponse deleteTask(Long id) {
        Task task = getTaskById(id);
        repository.delete(task);
        log.info("Task [{}] has been removed.", task.getTitle());
        return new MessageResponse("Task [" + task.getTitle() + "] has been removed.");
    }

    private Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    private Specification<Task> buildSpecificationFromFilter(TaskFilterDto filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.status() != null) {
                predicates.add(builder.equal(root.get("status"), filter.status()));
            }
            if (StringUtils.hasText(filter.category())) {
                predicates.add(builder.equal(root.get("category"), filter.category()));
            }
            if (filter.teamId() != null) {
                predicates.add(builder.equal(root.get("teamId").get("id"), filter.teamId()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
