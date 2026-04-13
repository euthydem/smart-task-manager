package com.jerif.smarttaskmanager.service.abstr;

import com.jerif.smarttaskmanager.dto.PrioritySuggestionResponse;

/**
 * Контракт сценариев ИИ для задач.
 */
public interface TaskAiService {

    /**
     * Формирует рекомендацию приоритета для существующей задачи.
     * @param taskId идентификатор задачи
     * @return ответ ИИ для слоя программного интерфейса
     */
    PrioritySuggestionResponse suggestPriority(Long taskId);
}
