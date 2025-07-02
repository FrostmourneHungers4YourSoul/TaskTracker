package com.example.tracker.controller;

import com.example.tracker.domain.model.Task;
import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PutMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable @NotNull Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> update(@PathVariable @NotNull Long id,
                                       @RequestBody Team team) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Team> delete(@PathVariable @NotNull Long id) {
        return null;
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Task> addTeamMember(@PathVariable @NotNull Long id,
                                              @RequestBody TeamMember member) {
        return null;
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<String> deleteTeamMember(@PathVariable @NotNull Long id,
                                                   @PathVariable @NotNull Long userId) {
        return null;
    }
}
