package com.islamnizami.taskboarddevlab.repository;

import com.islamnizami.taskboarddevlab.model.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findByUserIdAndIsDeletedFalse(Long userId);
    Optional<Label> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);

    boolean existsByNameIgnoreCaseAndUserIdAndIsDeletedFalse(String name, Long userId);
}