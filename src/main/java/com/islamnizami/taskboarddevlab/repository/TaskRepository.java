package com.islamnizami.taskboarddevlab.repository;


import com.islamnizami.taskboarddevlab.model.entity.Task;
import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);

    List<Task> findByUserIdAndStatusAndIsDeletedFalse(Long userId, TaskStatus status);

    List<Task> findByStatusNotAndDeadlineBetweenAndIsDeletedFalse(
            TaskStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}
