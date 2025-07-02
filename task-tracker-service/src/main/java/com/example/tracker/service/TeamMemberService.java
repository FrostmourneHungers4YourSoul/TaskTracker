package com.example.tracker.service;

import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import com.example.tracker.domain.model.User;

public interface TeamMemberService {
    TeamMember createMember(Team team, User user);

    void deleteMember(Team team, User user);
}
