package com.jerif.smarttaskmanager.mapper;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.dto.TaskCreateRequest;
import com.jerif.smarttaskmanager.dto.TaskResponse;
import com.jerif.smarttaskmanager.dto.TaskUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = TaskPriority.class)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "priority", source = "priority", defaultExpression = "java(TaskPriority.MEDIUM)")
    Task toEntity(TaskCreateRequest request);

    TaskResponse toResponse(Task task);

    List<TaskResponse> toResponses(List<Task> tasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateTaskFromRequest(TaskUpdateRequest request, @MappingTarget Task task);
}
