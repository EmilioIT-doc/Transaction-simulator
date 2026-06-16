package com.eglobal.simulador_transacciones.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String accessToken;
    private String tokenType;
    private String username;
    private String role;
}