package com.cafe.forecast.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
