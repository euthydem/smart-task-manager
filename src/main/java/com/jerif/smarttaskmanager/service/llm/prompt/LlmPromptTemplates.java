package com.jerif.smarttaskmanager.service.llm.prompt;

import com.jerif.smarttaskmanager.domain.Task;

/**
 * Хранилище шаблонов промптов для интеграции с языковой моделью.
 */
public final class LlmPromptTemplates {

    private static final String PRIORITY_SUGGESTION_SYSTEM_PROMPT = """
            You analyze tasks and determine their priority.
            Consider urgency, due date, and current status.
            Return only valid JSON without markdown.
            """;

    private static final String PRIORITY_SUGGESTION_USER_PROMPT_TEMPLATE = """
            Analyze the task and suggest its priority.

            Allowed priority values:
            LOW, MEDIUM, HIGH

            Return JSON in this format:
            {
              "suggestedPriority": "LOW | MEDIUM | HIGH",
              "reason": "short explanation"
            }

            Rules:
            - Use HIGH when the task has a near deadline or looks urgent
            - Use MEDIUM for normal work with moderate urgency
            - Use LOW for low-urgency tasks without strong deadline pressure
            - Keep the reason under 200 characters

            Task:
            title: %s
            description: %s
            dueDate: %s
            status: %s
            """;

    private LlmPromptTemplates() {
    }

    public static String prioritySuggestionSystemPrompt() {
        return PRIORITY_SUGGESTION_SYSTEM_PROMPT;
    }

    public static String prioritySuggestionUserPrompt(Task task) {
        return PRIORITY_SUGGESTION_USER_PROMPT_TEMPLATE.formatted(
                valueOrNull(task.getTitle()),
                valueOrNull(task.getDescription()),
                valueOrNull(task.getDueDate()),
                valueOrNull(task.getStatus())
        );
    }

    private static String valueOrNull(Object value) {
        return value == null ? "null" : value.toString();
    }
}
