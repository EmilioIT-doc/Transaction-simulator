package com.eglobal.simulador_transacciones.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO {

    private String rrn;
    private String stan;
    private String status;
    private String autorizacion;
    private String mensaje;
}