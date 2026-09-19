package com.islamnizami.taskboarddevlab.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelRequestDTO {

    @NotBlank(message = "Label name is required")
    @Size(max = 100, message = "Label name cannot exceed 100 characters")
    private String name;
}
