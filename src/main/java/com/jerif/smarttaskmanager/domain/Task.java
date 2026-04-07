package com.jerif.smarttaskmanager.domain;

import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Сущность задачи в системе управления задачами.
 * <p>
 * Хранит основную информацию о пользовательской задаче,
 * включая название, описание, приоритет, статус,
 * срок выполнения и время создания.
 * </p>
 */
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    /**
     * Уникальный идентификатор задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое название задачи.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Подробное описание задачи.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Приоритет задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    /**
     * Текущий статус задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    /**
     * Срок выполнения задачи.
     */
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    /**
     * Время создания задачи.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
