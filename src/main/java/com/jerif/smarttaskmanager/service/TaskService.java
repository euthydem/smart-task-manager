package com.jerif.smarttaskmanager.service;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import com.jerif.smarttaskmanager.dto.TaskCreateRequest;
import com.jerif.smarttaskmanager.dto.TaskResponse;
import com.jerif.smarttaskmanager.dto.TaskUpdateRequest;
import com.jerif.smarttaskmanager.exception.TaskNotFoundException;
import com.jerif.smarttaskmanager.mapper.TaskMapper;
import com.jerif.smarttaskmanager.repository.TaskRepository;
import com.jerif.smarttaskmanager.repository.TaskSpecifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервисный слой для CRUD-операций над задачами.
 */
@Service
@Slf4j
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Создаёт экземпляр сервиса задач.
     * @param taskRepository репозиторий для доступа к задачам
     */
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Создаёт новую задачу.
     * @param request запрос с данными задачи
     * @return DTO созданной задачи
     */
    public TaskResponse createTask(TaskCreateRequest request) {
        log.info("Создание задачи: title='{}', priority={}, dueDate={}",
                request.getTitle(), request.getPriority(), request.getDueDate());

        Task savedTask = taskRepository.save(taskMapper.toEntity(request));
        log.info("Задача создана: id={}", savedTask.getId());
        return taskMapper.toResponse(savedTask);
    }

    /**
     * Возвращает задачу по идентификатору.
     * @param id идентификатор задачи
     * @return DTO найденной задачи
     */
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        log.debug("Получение задачи по id={}", id);

        return taskMapper.toResponse(findTaskById(id));
    }

    /**
     * Возвращает список всех задач.
     * @return список DTO задач
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueBefore,
            String search
    ) {
        log.debug("Получение списка задач с фильтрами: status={}, priority={}, dueBefore={}, search={}",
                status, priority, dueBefore, search);

        List<TaskResponse> tasks = taskMapper.toResponses(
                taskRepository.findAll(TaskSpecifications.withFilters(status, priority, dueBefore, search))
        );
        log.debug("Получен список задач, количество={}", tasks.size());
        return tasks;
    }

    /**
     * Обновляет существующую задачу.
     * @param id идентификатор задачи
     * @param request запрос с полями для обновления
     * @return DTO обновлённой задачи
     */
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        log.info("Обновление задачи id={}", id);
        Task task = findTaskById(id);

        taskMapper.updateTaskFromRequest(request, task);

        Task updatedTask = taskRepository.save(task);
        log.info("Задача обновлена: id={}", updatedTask.getId());
        return taskMapper.toResponse(updatedTask);
    }

    /**
     * Удаляет задачу по идентификатору.
     * @param id идентификатор задачи
     */
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
        log.info("Задача удалена: id={}", id);
    }

    /**
     * Ищет задачу по идентификатору или выбрасывает доменное исключение.
     * @param id идентификатор задачи
     * @return найденная сущность задачи
     */
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Задача не найдена: id={}", id);
                    return new TaskNotFoundException(id);
                });
    }

}
