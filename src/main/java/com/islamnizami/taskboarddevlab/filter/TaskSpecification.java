package com.islamnizami.taskboarddevlab.filter;

import com.islamnizami.taskboarddevlab.model.entity.Label;
import com.islamnizami.taskboarddevlab.model.entity.Task;
import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> filterTasks(
            Long userId,
            TaskStatus status,
            String keyword,
            Long labelId,
            LocalDateTime deadline) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate titleMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(titleMatch, descMatch));
            }

            if (labelId != null) {
                Join<Task, Label> labels = root.join("labels");
                predicates.add(criteriaBuilder.equal(labels.get("id"), labelId));
                query.distinct(true);
            }

            if (deadline != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deadline"), deadline));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}