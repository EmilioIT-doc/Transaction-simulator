package com.eglobal.simulador_transacciones.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para el endpoint de historial, incluye todos los campos relevantes de la transacción
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionHistorialDTO {

    private Long id;
    private String rrn;
    private String stan;
    private BigDecimal importe;
    private String fecha;
    private String statusRespuesta;
    private String autorizacion;
    private LocalDateTime createdAt;
}
