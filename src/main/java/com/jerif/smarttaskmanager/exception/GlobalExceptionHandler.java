package com.jerif.smarttaskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений REST API с единым форматом ответа.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обрабатывает ошибки отсутствия сущности.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTaskNotFound(
            TaskNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Ресурс не найден: path={}, message={}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /**
     * Обрабатывает ошибки валидации тела запроса.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return ответ с описанием ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Ошибка валидации запроса: path={}, errors={}", request.getRequestURI(), validationErrors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации", request, validationErrors);
    }

    /**
     * Обрабатывает некорректный JSON и ошибки разбора enum или даты.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        log.warn("Некорректное тело запроса: path={}, message={}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Тело запроса имеет неверный формат", request, null);
    }

    /**
     * Обрабатывает ошибки преобразования path variable и query параметров.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String message = "Параметр '%s' имеет недопустимое значение '%s'"
                .formatted(ex.getName(), ex.getValue());
        log.warn("Некорректный параметр запроса: path={}, {}", request.getRequestURI(), message);
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    /**
     * Обрабатывает непредвиденные серверные ошибки.
     * @param ex выброшенное исключение
     * @param request текущий HTTP-запрос
     * @return ответ с описанием ошибки
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Внутренняя ошибка сервера: path={}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request, null);
    }

    /**
     * Формирует стандартное тело ответа с ошибкой.
     *
     * @param status HTTP-статус ответа
     * @param message текст ошибки
     * @param request текущий HTTP-запрос
     * @param validationErrors детали ошибок валидации, если есть
     * @return ответ с унифицированным телом ошибки
     */
    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> validationErrors
    ) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
