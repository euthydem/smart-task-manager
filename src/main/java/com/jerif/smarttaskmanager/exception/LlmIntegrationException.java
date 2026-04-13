package com.jerif.smarttaskmanager.exception;

/**
 * Сигнализирует об ошибке интеграции с внешней языковой моделью.
 */
public class LlmIntegrationException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением.
     * @param message текст исключения
     */
    public LlmIntegrationException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и причиной.
     * @param message текст исключения
     * @param cause исходная причина
     */
    public LlmIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
