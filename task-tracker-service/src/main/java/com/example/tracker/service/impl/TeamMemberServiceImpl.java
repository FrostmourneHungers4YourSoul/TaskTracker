package com.example.tracker.service.impl;

import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import com.example.tracker.domain.model.User;
import com.example.tracker.exception.ResourceAlreadyExistsException;
import com.example.tracker.exception.ResourceNotFoundException;
import com.example.tracker.repository.TeamMemberRepository;
import com.example.tracker.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamMemberRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TeamMember createMember(Team team, User user) {
        if (repository.existsByTeamIdAndUserId(team.getId(), user.getId()))
            throw new ResourceAlreadyExistsException("User is already in the team.");

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        return repository.save(teamMember);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteMember(Team team, User user) {
        TeamMember teamMember = repository.getTeamMemberByUserAndTeam(user, team)
                .orElseThrow(() -> new ResourceNotFoundException("Team member is not found."));
        repository.delete(teamMember);
    }
}
