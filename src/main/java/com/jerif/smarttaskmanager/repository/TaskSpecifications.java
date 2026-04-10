package com.jerif.smarttaskmanager.repository;

import com.jerif.smarttaskmanager.domain.Task;
import com.jerif.smarttaskmanager.domain.enums.TaskPriority;
import com.jerif.smarttaskmanager.domain.enums.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> withFilters(
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueBefore,
            String search
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (dueBefore != null) {
                LocalDateTime exclusiveUpperBound = dueBefore.plusDays(1).atStartOfDay();
                predicates.add(criteriaBuilder.lessThan(root.get("dueDate").as(LocalDateTime.class), exclusiveUpperBound));
            }

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Predicate titleLike = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title").as(String.class)),
                        pattern
                );
                Predicate descriptionLike = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description").as(String.class)),
                        pattern
                );
                predicates.add(criteriaBuilder.or(titleLike, descriptionLike));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
