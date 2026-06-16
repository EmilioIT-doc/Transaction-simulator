package com.eglobal.simulador_transacciones.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "El password es requerido")
    private String password;
}