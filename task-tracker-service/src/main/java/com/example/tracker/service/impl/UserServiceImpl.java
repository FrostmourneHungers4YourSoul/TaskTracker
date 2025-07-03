package com.example.tracker.service.impl;

import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.UserResponseDto;
import com.example.tracker.domain.model.User;
import com.example.tracker.domain.model.enums.Role;
import com.example.tracker.exception.ResourceAlreadyExistsException;
import com.example.tracker.exception.ResourceNotFoundException;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto requestDto) {
        if (repository.existsByUsername(requestDto.username()))
            throw new ResourceAlreadyExistsException("User with this username already exists.");

        if (repository.existsByEmail(requestDto.email()))
            throw new ResourceAlreadyExistsException("User with this email already exists.");

        User user = mapper.toEntity(requestDto);

        user.setRole(requestDto.role());
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        LocalDateTime localDateTime = LocalDateTime.now();
        user.setCreatedAt(localDateTime);
        user.setUpdatedAt(localDateTime);

        user = repository.save(user);
        log.info("Created new user: [id={}, username={}, createdAt={}]", user.getId(), user.getUsername(), user.getCreatedAt());
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = getById(id);
        log.info("Found user: {}", user);
        return mapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User currentUser = getById(id);

        User updatedUser = mapper.toEntity(requestDto);

        updatedUser.setId(currentUser.getId());
        updatedUser.setCreatedAt(currentUser.getCreatedAt());
        updatedUser.setUpdatedAt(LocalDateTime.now());

        if (StringUtils.hasText(requestDto.password()))
            updatedUser.setPassword(passwordEncoder.encode(requestDto.password()));

        if (currentUser.getRole() == Role.ADMIN)
            updatedUser.setRole(requestDto.role());
        else
            updatedUser.setRole(currentUser.getRole());

        updatedUser = repository.save(updatedUser);

        log.info("User [{}] has been updated: {}", updatedUser.getUsername(), updatedUser);
        return mapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public MessageResponse deleteUser(Long id) {
        User user = getById(id);
        repository.delete(user);
        log.info("User [{}] has been removed.", user.getUsername());
        return new MessageResponse("User [" + user.getUsername() + "] has been removed.");
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}
