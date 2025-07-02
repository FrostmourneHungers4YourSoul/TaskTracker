package com.example.tracker.service;

import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.UserResponseDto;
import com.example.tracker.domain.model.User;

import java.util.List;

public interface UserService {
    UserResponseDto getUser(Long id);
    List<UserResponseDto> getUsers();
    UserResponseDto updateUser(Long id, UserRequestDto requestDto);
    MessageResponse deleteUser(Long id);

    UserResponseDto create(UserRequestDto requestDto);

    User getUserByUsername(String username);
    User getUserById(Long id);
}
