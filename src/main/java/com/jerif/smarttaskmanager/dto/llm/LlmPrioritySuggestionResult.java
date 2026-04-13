package com.jerif.smarttaskmanager.dto.llm;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Внутренний объект передачи данных для структурированного ответа языковой модели с рекомендацией приоритета.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmPrioritySuggestionResult {

    private TaskPriority suggestedPriority;
    private String reason;
}
