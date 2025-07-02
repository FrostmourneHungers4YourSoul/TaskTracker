package com.example.tracker.service.impl;

import com.example.tracker.domain.dto.request.TeamMemberRequestDto;
import com.example.tracker.domain.dto.request.TeamRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TeamMemberResponseDto;
import com.example.tracker.domain.dto.response.TeamResponseDto;
import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import com.example.tracker.domain.model.User;
import com.example.tracker.exception.ResourceAlreadyExistsException;
import com.example.tracker.exception.ResourceNotFoundException;
import com.example.tracker.mapper.TeamMapper;
import com.example.tracker.repository.TeamRepository;
import com.example.tracker.service.TeamMemberService;
import com.example.tracker.service.TeamService;
import com.example.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberService teamMemberService;
    private final UserService userService;
    private final TeamMapper mapper;


    @Override
    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto requestDto) {
        if (teamRepository.existsTeamByName(requestDto.name()))
            throw new ResourceAlreadyExistsException(
                    "Team [" + requestDto.name() + "] is already exists.");

        Team newTeam = mapper.toEntity(requestDto);
        User creator = userService.getUserById(requestDto.creatorId());
        newTeam.setCreatedBy(creator);

        newTeam = teamRepository.save(newTeam);
        log.info("Created Team: {}", newTeam);
        return mapper.toDto(newTeam);
    }

    @Override
    public List<TeamResponseDto> getTeams() {
        return teamRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public TeamResponseDto getTeam(Long id) {
        Team team = getTeamById(id);
        log.info("Found team: {}", team);
        return mapper.toDto(team);
    }

    @Override
    @Transactional
    public TeamResponseDto updateTeam(Long id, TeamRequestDto requestDto) {
        Team updatingTeam = getTeamById(id);
        User creator = userService.getUserById(requestDto.creatorId());
        updatingTeam.setCreatedBy(creator);
        updatingTeam.setUpdatedAt(LocalDateTime.now());

        Team updatedTeam = teamRepository.save(updatingTeam);
        log.info("Team updated: {}", updatedTeam);
        return mapper.toDto(updatedTeam);
    }

    @Override
    @Transactional
    public MessageResponse removeTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.delete(team);
        log.info("Team [{}] has been removed.", team.getName());
        return new MessageResponse("Team [" + team.getName() + "] has been removed.");
    }

    @Override
    public TeamMemberResponseDto addTeamMember(Long id, TeamMemberRequestDto memberRequest) {
        Team team = getTeamById(id);
        User user = userService.getUserById(memberRequest.userId());

        TeamMember teamMember = teamMemberService.createMember(team, user);
        log.info("User [{}] was added to team [{}]", user.getUsername(), team.getName());
        return mapper.toTeamMemberResponseDto(teamMember);
    }

    @Override
    public MessageResponse deleteTeamMember(Long id, Long userId) {
        Team team = getTeamById(id);
        User user = userService.getUserById(userId);

        teamMemberService.deleteMember(team, user);
        log.info("User [{}] was removed from team [{}]", user.getUsername(), team.getName());
        return new MessageResponse("User ["+ user.getUsername() +"] was removed from team [" + team.getName() +"]");
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found."));
    }
}
