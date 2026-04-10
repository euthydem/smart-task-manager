package com.jerif.smarttaskmanager.dto;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных задачи между слоями приложения.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
}
