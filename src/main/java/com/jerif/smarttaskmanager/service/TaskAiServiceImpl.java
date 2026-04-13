package com.jerif.smarttaskmanager.service;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.dto.PrioritySuggestionResponse;
import com.jerif.smarttaskmanager.dto.llm.LlmPrioritySuggestionResult;
import com.jerif.smarttaskmanager.service.abstr.TaskAiService;
import com.jerif.smarttaskmanager.service.abstr.TaskService;
import com.jerif.smarttaskmanager.service.llm.abstr.LlmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Координирует сценарии ИИ для задач.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class TaskAiServiceImpl implements TaskAiService {

    private final TaskService taskService;
    private final LlmService llmService;

    /**
     * Создаёт экземпляр сервиса ИИ для задач.
     * @param taskService сервис загрузки задач
     * @param llmService сервис интеграции с языковой моделью
     */
    public TaskAiServiceImpl(TaskService taskService, LlmService llmService) {
        this.taskService = taskService;
        this.llmService = llmService;
    }

    /**
     * Возвращает рекомендацию приоритета для существующей задачи.
     */
    @Override
    public PrioritySuggestionResponse suggestPriority(Long taskId) {
        log.info("Generating AI priority suggestion for task id={}", taskId);

        Task task = taskService.findTaskById(taskId);
        LlmPrioritySuggestionResult llmResult = llmService.suggestPriority(task);

        return PrioritySuggestionResponse.builder()
                .taskId(task.getId())
                .suggestedPriority(llmResult.getSuggestedPriority())
                .reason(llmResult.getReason())
                .build();
    }
}
