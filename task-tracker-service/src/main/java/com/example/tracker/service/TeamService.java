package com.example.tracker.service;

import com.example.tracker.domain.dto.request.TeamMemberRequestDto;
import com.example.tracker.domain.dto.request.TeamRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TeamMemberResponseDto;
import com.example.tracker.domain.dto.response.TeamResponseDto;
import com.example.tracker.domain.model.Team;

import java.util.List;

public interface TeamService {
    TeamResponseDto createTeam(Long userId, TeamRequestDto requestDto);

    List<TeamResponseDto> getTeams();

    TeamResponseDto getTeam(Long id);

    TeamResponseDto updateTeam(Long id, TeamRequestDto requestDto);

    MessageResponse removeTeam(Long id);

    TeamMemberResponseDto addTeamMember(Long id, TeamMemberRequestDto memberRequest);

    MessageResponse deleteTeamMember(Long id, Long userId);

    Team getTeamById(Long aLong);
}
