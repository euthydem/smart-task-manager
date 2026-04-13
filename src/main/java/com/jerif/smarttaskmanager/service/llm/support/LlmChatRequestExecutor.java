package com.jerif.smarttaskmanager.service.llm.support;

import com.jerif.smarttaskmanager.exception.LlmIntegrationException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;

/**
 * Выполняет запросы к языковой модели и преобразует ошибки провайдера
 * в исключения интеграционного слоя.
 */
@Component
public class LlmChatRequestExecutor {

    private final ChatClient chatClient;

    public LlmChatRequestExecutor(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String requestJson(String systemPrompt, String userPrompt, OpenAiChatOptions options) {
        try {
            return chatClient.prompt()
                    .options(options)
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();
        } catch (RuntimeException e) {
            throw new LlmIntegrationException("Failed to call LLM provider", e);
        }
    }
}
