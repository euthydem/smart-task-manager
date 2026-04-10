package com.jerif.smarttaskmanager.mapper;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import com.jerif.smarttaskmanager.dto.TaskCreateRequest;
import com.jerif.smarttaskmanager.dto.TaskUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void shouldMapCreateRequestWithDefaults() {
        TaskCreateRequest request = TaskCreateRequest.builder()
                .title("Prepare release")
                .description("Backend release candidate")
                .dueDate(LocalDateTime.of(2026, 4, 12, 18, 0))
                .build();

        Task task = taskMapper.toEntity(request);

        assertEquals("Prepare release", task.getTitle());
        assertEquals("Backend release candidate", task.getDescription());
        assertEquals(TaskPriority.MEDIUM, task.getPriority());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals(LocalDateTime.of(2026, 4, 12, 18, 0), task.getDueDate());
        assertNull(task.getId());
        assertNull(task.getCreatedAt());
    }

    @Test
    void shouldIgnoreNullFieldsOnUpdate() {
        Task task = new Task();
        task.setTitle("Current title");
        task.setDescription("Current description");
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDateTime.of(2026, 4, 20, 10, 0));

        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .description("Updated description")
                .status(TaskStatus.DONE)
                .build();

        taskMapper.updateTaskFromRequest(request, task);

        assertEquals("Current title", task.getTitle());
        assertEquals("Updated description", task.getDescription());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(LocalDateTime.of(2026, 4, 20, 10, 0), task.getDueDate());
    }
}
