package com.jerif.smarttaskmanager.service.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.dto.llm.LlmPrioritySuggestionResult;
import com.jerif.smarttaskmanager.exception.LlmIntegrationException;
import com.jerif.smarttaskmanager.service.llm.abstr.LlmService;
import com.jerif.smarttaskmanager.service.llm.prompt.LlmPromptTemplates;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

@Service
public class GroqLlmService implements LlmService {

    private static final OpenAiChatOptions JSON_RESPONSE_OPTIONS = OpenAiChatOptions.builder()
            .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
            .build();

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public GroqLlmService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public LlmPrioritySuggestionResult suggestPriority(Task task) {
        String content = requestPrioritySuggestion(task);
        return parsePrioritySuggestion(content);
    }

    private String requestPrioritySuggestion(Task task) {
        try {
            return chatClient.prompt()
                    .options(JSON_RESPONSE_OPTIONS)
                    .system(LlmPromptTemplates.prioritySuggestionSystemPrompt())
                    .user(LlmPromptTemplates.prioritySuggestionUserPrompt(task))
                    .call()
                    .content();
        } catch (RuntimeException e) {
            throw new LlmIntegrationException("Failed to call LLM provider", e);
        }
    }

    private LlmPrioritySuggestionResult parsePrioritySuggestion(String content) {
        if (content == null || content.isBlank()) {
            throw new LlmIntegrationException("LLM returned empty response");
        }

        try {
            return objectMapper.readValue(content, LlmPrioritySuggestionResult.class);
        } catch (JsonProcessingException e) {
            throw new LlmIntegrationException("LLM returned invalid JSON response", e);
        }
    }
}
