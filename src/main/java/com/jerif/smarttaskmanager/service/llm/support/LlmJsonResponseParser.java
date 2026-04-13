package com.jerif.smarttaskmanager.service.llm.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerif.smarttaskmanager.exception.LlmIntegrationException;
import org.springframework.stereotype.Component;

/**
 * Разбирает JSON-ответы языковой модели в типизированные объекты.
 */
@Component
public class LlmJsonResponseParser {

    private final ObjectMapper objectMapper;

    public LlmJsonResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T parseRequiredJson(String content, Class<T> targetType) {
        if (content == null || content.isBlank()) {
            throw new LlmIntegrationException("LLM returned empty response");
        }

        try {
            return objectMapper.readValue(content, targetType);
        } catch (JsonProcessingException e) {
            throw new LlmIntegrationException("LLM returned invalid JSON response", e);
        }
    }
}
