package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.TeamMemberRequestDto;
import com.example.tracker.domain.dto.request.TeamRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TeamMemberResponseDto;
import com.example.tracker.domain.dto.response.TeamResponseDto;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponseDto> create(@RequestBody @Valid TeamRequestDto requestDto,
                                                  @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(teamService.createTeam(user.getId(), requestDto));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getTeams() {
        return ResponseEntity.ok(teamService.getTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(teamService.getTeam(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponseDto> update(@PathVariable @NotNull Long id,
                                                  @RequestBody TeamRequestDto requestDto) {
        return ResponseEntity.ok(teamService.updateTeam(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(@PathVariable @NotNull Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(teamService.removeTeam(id));
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamMemberResponseDto> addMember(@PathVariable @NotNull Long id,
                                                           @RequestBody TeamMemberRequestDto memberRequest) {
        return ResponseEntity.ok(teamService.addTeamMember(id, memberRequest));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MessageResponse> deleteMember(@PathVariable @NotNull Long id,
                                                        @PathVariable @NotNull Long userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(teamService.deleteTeamMember(id, userId));
    }
}
