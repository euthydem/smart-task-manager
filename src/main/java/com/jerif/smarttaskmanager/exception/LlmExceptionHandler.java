package com.jerif.smarttaskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Выделенный обработчик ошибок интеграции с языковой моделью.
 */
@RestControllerAdvice
@Order(0)
public class LlmExceptionHandler {

    /**
     * Преобразует ошибки интеграции с языковой моделью в ответ с кодом 502.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return унифицированный ответ с ошибкой
     */
    @ExceptionHandler(LlmIntegrationException.class)
    public ResponseEntity<ApiErrorResponse> handleLlmIntegrationException(
            LlmIntegrationException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_GATEWAY.value())
                .error(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .validationErrors(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }
}
