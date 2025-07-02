package com.example.tracker.mapper;

import com.example.tracker.domain.dto.request.TaskRequestDto;
import com.example.tracker.domain.dto.response.TaskResponseDto;
import com.example.tracker.domain.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, TeamMapper.class}
)
public interface TaskMapper {

    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "team", ignore = true)
    Task toEntity(TaskRequestDto taskRequest);

    TaskResponseDto toDto(Task task);
}
