package com.islamnizami.taskboarddevlab.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {

    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";
}
