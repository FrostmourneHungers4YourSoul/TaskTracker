package com.example.tracker.mapper;

import com.example.tracker.domain.dto.request.TeamRequestDto;
import com.example.tracker.domain.dto.response.TeamMemberResponseDto;
import com.example.tracker.domain.dto.response.TeamResponseDto;
import com.example.tracker.domain.model.Team;
import com.example.tracker.domain.model.TeamMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = UserMapper.class
)
public interface TeamMapper {

    @Mapping(target = "createdBy", ignore = true)
    Team toEntity(TeamRequestDto requestDto);

    TeamResponseDto toDto(Team newTeam);

    TeamMemberResponseDto toTeamMemberResponseDto(TeamMember teamMember);
}
