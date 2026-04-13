package com.jerif.smarttaskmanager.service.llm.abstr;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.dto.llm.LlmPrioritySuggestionResult;

/**
 * Контракт интеграционного сервиса для получения рекомендации приоритета от языковой модели.
 */
public interface LlmService {

    /**
     * Возвращает рекомендацию приоритета для переданной задачи.
     *
     * @param task задача для анализа
     * @return структурированный результат ответа языковой модели
     */
    LlmPrioritySuggestionResult suggestPriority(Task task);
}
