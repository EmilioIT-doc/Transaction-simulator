package com.eglobal.simulador_transacciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rrn;

    @Column(nullable = false)
    private String stan;

    @Column(nullable = false)
    private BigDecimal importe;

    @Column(nullable = false)
    private String fecha;

    @Column(name = "status_respuesta")
    private String statusRespuesta;

    @Column(name = "autorizacion")
    private String autorizacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relación con el usuario que creó la transacción, permite filtrar el historial por usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}