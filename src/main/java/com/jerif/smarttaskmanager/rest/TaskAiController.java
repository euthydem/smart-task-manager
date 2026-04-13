package com.jerif.smarttaskmanager.rest;

import com.jerif.smarttaskmanager.dto.PrioritySuggestionResponse;
import com.jerif.smarttaskmanager.service.abstr.TaskAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Веб-контроллер для операций ИИ над задачами.
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/tasks")
public class TaskAiController {

    private final TaskAiService taskAiService;

    /**
     * Создаёт экземпляр AI-контроллера задач.
     * @param taskAiService сервис сценариев ИИ для задач
     */
    public TaskAiController(TaskAiService taskAiService) {
        this.taskAiService = taskAiService;
    }

    /**
     * Запрашивает у AI рекомендацию приоритета для задачи.
     * @param id идентификатор задачи
     * @return ответ с рекомендованным приоритетом
     */
    @PostMapping("/{id}/llm/suggest-priority")
    public ResponseEntity<PrioritySuggestionResponse> suggestPriority(@PathVariable Long id) {
        log.debug("Requesting AI priority suggestion for task id={}", id);
        return ResponseEntity.ok(taskAiService.suggestPriority(id));
    }
}
