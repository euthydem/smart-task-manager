package com.jerif.smarttaskmanager.dto;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Объект передачи данных запроса на обновление задачи.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private LocalDateTime dueDate;
}
