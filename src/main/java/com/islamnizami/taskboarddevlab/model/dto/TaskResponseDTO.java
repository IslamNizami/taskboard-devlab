package com.islamnizami.taskboarddevlab.model.dto;
import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Set<LabelResponseDTO> labels;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}