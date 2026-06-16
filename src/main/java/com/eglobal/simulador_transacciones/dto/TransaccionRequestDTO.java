package com.eglobal.simulador_transacciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionRequestDTO {

    @NotBlank(message = "El RRN es requerido")
    @Size(min = 12, max = 12, message = "El RRN debe tener exactamente 12 dígitos")
    @Pattern(regexp = "\\d{12}", message = "El RRN debe contener solo dígitos")
    private String rrn;

    @NotBlank(message = "El STAN es requerido")
    @Size(min = 6, max = 6, message = "El STAN debe tener exactamente 6 dígitos")
    @Pattern(regexp = "\\d{6}", message = "El STAN debe contener solo dígitos")
    private String stan;

    @NotNull(message = "El importe es requerido")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor a 0")
    @DecimalMax(value = "99999999999.99", message = "El importe excede el máximo permitido")
    @Digits(integer = 11, fraction = 2, message = "El importe debe tener máximo 11 enteros y 2 decimales")
    private BigDecimal importe;

    @NotBlank(message = "La fecha es requerida")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD")
    private String fecha;
}