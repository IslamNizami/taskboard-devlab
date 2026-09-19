package com.islamnizami.taskboarddevlab.model.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelResponseDTO {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
