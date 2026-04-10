package com.jerif.smarttaskmanager.exception;

/**
 * Исключение, выбрасываемое при отсутствии задачи по идентификатору.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Создаёт исключение для отсутствующей задачи.
     * @param id идентификатор задачи
     */
    public TaskNotFoundException(Long id) {
        super("Задача с id %d не найдена".formatted(id));
    }
}
