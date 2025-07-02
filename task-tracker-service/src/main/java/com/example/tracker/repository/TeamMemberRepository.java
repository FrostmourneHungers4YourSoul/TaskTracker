package com.example.tracker.repository;

import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import com.example.tracker.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndUserId(Long teamId, Long userId);

    Optional<TeamMember> getTeamMemberByUserAndTeam(User user, Team team);
}
