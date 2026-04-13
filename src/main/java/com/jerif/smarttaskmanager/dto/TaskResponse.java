package com.jerif.smarttaskmanager.dto;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Объект передачи данных ответа с информацией о задаче.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
}
