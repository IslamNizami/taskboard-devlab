package com.islamnizami.taskboarddevlab.model.dto;


import com.islamnizami.taskboarddevlab.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotBlank(message = "TItle is required")
    @Size(max = 255,message = "Title must not exceed 255 characters.")
    private String title;

    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Set<Long> labelIds;
}
