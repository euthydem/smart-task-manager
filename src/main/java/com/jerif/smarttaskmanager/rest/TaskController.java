package com.jerif.smarttaskmanager.rest;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import com.jerif.smarttaskmanager.dto.TaskCreateRequest;
import com.jerif.smarttaskmanager.dto.TaskResponse;
import com.jerif.smarttaskmanager.dto.TaskUpdateRequest;
import com.jerif.smarttaskmanager.service.TaskService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST-контроллер для CRUD-операций над задачами.
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Создаёт экземпляр контроллера задач.
     * @param taskService сервис для работы с задачами
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Создаёт новую задачу.
     * @param request валидированный запрос на создание
     * @return ответ с созданной задачей
     */
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody TaskCreateRequest request) {
        log.debug("Creating new task");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    /**
     * Возвращает задачу по идентификатору.
     * @param id идентификатор задачи
     * @return ответ с найденной задачей
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        log.debug("Fetching task id={}", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Возвращает список всех задач.
     * @return ответ со списком задач
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
            @RequestParam(required = false) String search
    ) {
        log.debug("Fetching tasks, filters: status={}, priority={}, dueBefore={}, search={}",
                status, priority, dueBefore, search);
        return ResponseEntity.ok(taskService.getAllTasks(status, priority, dueBefore, search));
    }

    /**
     * Обновляет существующую задачу.
     * @param id идентификатор задачи
     * @param request валидированный запрос на обновление
     * @return ответ с обновлённой задачей
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        log.debug("Updating task id={}", id);
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    /**
     * Удаляет задачу по идентификатору.
     * @param id идентификатор задачи
     * @return пустой ответ со статусом 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("Deleting task id={}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
