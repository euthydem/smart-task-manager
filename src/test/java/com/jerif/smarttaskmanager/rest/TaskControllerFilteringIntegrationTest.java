package com.jerif.smarttaskmanager.rest;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import com.jerif.smarttaskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerFilteringIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldFilterTasksUsingCombinedQueryParameters() throws Exception {
        Task matchingTask = task(
                "Pay electricity bill",
                "Apartment payment for April",
                TaskPriority.HIGH,
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2026, 4, 15, 10, 0)
        );
        Task wrongPriority = task(
                "Pay internet bill",
                "Apartment payment for April",
                TaskPriority.MEDIUM,
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2026, 4, 15, 9, 0)
        );
        Task wrongDueDate = task(
                "Pay rent",
                "Monthly payment",
                TaskPriority.HIGH,
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2026, 4, 16, 9, 0)
        );
        taskRepository.saveAll(List.of(matchingTask, wrongPriority, wrongDueDate));

        mockMvc.perform(get("/api/v1/tasks")
                        .queryParam("status", "IN_PROGRESS")
                        .queryParam("priority", "HIGH")
                        .queryParam("dueBefore", "2026-04-15")
                        .queryParam("search", "payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Pay electricity bill"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    void shouldSearchByTitleOrDescriptionCaseInsensitively() throws Exception {
        Task titleMatch = task(
                "Prepare Demo",
                "Sprint review notes",
                TaskPriority.MEDIUM,
                TaskStatus.PENDING,
                LocalDateTime.of(2026, 4, 20, 12, 0)
        );
        Task descriptionMatch = task(
                "Finalize report",
                "Need DEMO screenshots for appendix",
                TaskPriority.LOW,
                TaskStatus.DONE,
                LocalDateTime.of(2026, 4, 18, 18, 0)
        );
        Task noMatch = task(
                "Book hotel",
                "Travel arrangement",
                TaskPriority.HIGH,
                TaskStatus.PENDING,
                LocalDateTime.of(2026, 4, 25, 9, 0)
        );
        taskRepository.saveAll(List.of(titleMatch, descriptionMatch, noMatch));

        mockMvc.perform(get("/api/v1/tasks")
                        .queryParam("search", "demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private Task task(
            String title,
            String description,
            TaskPriority priority,
            TaskStatus status,
            LocalDateTime dueDate
    ) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueDate(dueDate);
        return task;
    }
}
