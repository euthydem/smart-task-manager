package com.jerif.smarttaskmanager.service.abstr;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import com.jerif.smarttaskmanager.dto.TaskCreateRequest;
import com.jerif.smarttaskmanager.dto.TaskResponse;
import com.jerif.smarttaskmanager.dto.TaskUpdateRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Контракт сервисного слоя для операций создания, чтения, обновления и удаления задач.
 */
public interface TaskService {

    /**
     * Создаёт новую задачу.
     * @param request запрос с данными задачи
     * @return объект передачи данных созданной задачи
     */
    TaskResponse createTask(TaskCreateRequest request);

    /**
     * Возвращает задачу по идентификатору.
     * @param id идентификатор задачи
     * @return объект передачи данных найденной задачи
     */
    TaskResponse getTaskById(Long id);

    /**
     * Возвращает список задач с учётом фильтров.
     * @param status фильтр по статусу
     * @param priority фильтр по приоритету
     * @param dueBefore верхняя граница даты выполнения
     * @param search строка поиска по названию и описанию
     * @return список объектов передачи данных задач
     */
    List<TaskResponse> getAllTasks(
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueBefore,
            String search
    );

    /**
     * Обновляет существующую задачу.
     * @param id идентификатор задачи
     * @param request запрос с полями для обновления
     * @return объект передачи данных обновлённой задачи
     */
    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    /**
     * Удаляет задачу по идентификатору.
     * @param id идентификатор задачи
     */
    void deleteTask(Long id);

    /**
     * Возвращает сущность задачи по идентификатору.
     * @param id идентификатор задачи
     * @return найденная сущность задачи
     */
    Task findTaskById(Long id);
}
