package com.example.tracker.controller;

import com.example.tracker.domain.dto.request.TeamMemberRequestDto;
import com.example.tracker.domain.dto.request.TeamRequestDto;
import com.example.tracker.domain.dto.response.MessageResponse;
import com.example.tracker.domain.dto.response.TeamMemberResponseDto;
import com.example.tracker.domain.dto.response.TeamResponseDto;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API Teams", description = "Управление командами")
public class TeamController {
    private final TeamService teamService;

    @Operation(
            summary = "Создать команду",
            description = "Доступ только ADMIN и MANAGER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команда успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "403", description = "Нет прав на создание команды")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponseDto> create(
            @Parameter(description = "Данные команды", required = true)
            @RequestBody @Valid TeamRequestDto requestDto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(teamService.createTeam(user.getId(), requestDto));
    }

    @Operation(summary = "Получить список команд")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список команд",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = TeamResponseDto.class
                                            )
                                    )
                            )
                    }
            )
    })
    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getTeams() {
        return ResponseEntity.ok(teamService.getTeams());
    }

    @Operation(summary = "Получить команду по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команда найдена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeam(
            @Parameter(description = "ID команды", required = true)
            @PathVariable @NotNull Long id) {
        return ResponseEntity.ok(teamService.getTeam(id));
    }

    @Operation(
            summary = "Обновить команду по ID",
            description = "Доступ только ADMIN и MANAGER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Команда обновлена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена"),
            @ApiResponse(responseCode = "403", description = "Нет прав на обновление команды")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponseDto> update(
            @Parameter(description = "ID команды", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Обновлённые данные команды", required = true)
            @RequestBody TeamRequestDto requestDto) {
        return ResponseEntity.ok(teamService.updateTeam(id, requestDto));
    }

    @Operation(
            summary = "Удалить команду по ID",
            description = "Доступ только ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Команда удалена"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(
            @Parameter(description = "ID команды", required = true)
            @PathVariable @NotNull Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(teamService.removeTeam(id));
    }

    @Operation(
            summary = "Добавить участника в команду",
            description = "Доступ только ADMIN и MANAGER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Участник добавлен"),
            @ApiResponse(responseCode = "404", description = "Команда или пользователь не найдены")
    })
    @PostMapping("/{id}/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamMemberResponseDto> addMember(
            @Parameter(description = "ID команды", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "Информация об участнике", required = true)
            @RequestBody TeamMemberRequestDto memberRequest) {
        return ResponseEntity.ok(teamService.addTeamMember(id, memberRequest));
    }

    @Operation(
            summary = "Удалить участника из команды",
            description = "Доступ только ADMIN и MANAGER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Участник удалён из команды"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Команда или пользователь не найдены")
    })
    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MessageResponse> deleteMember(
            @Parameter(description = "ID команды", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable @NotNull Long userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(teamService.deleteTeamMember(id, userId));
    }
}
