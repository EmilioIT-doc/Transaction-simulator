package com.eglobal.simulador_transacciones.repository;

import com.eglobal.simulador_transacciones.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByRrn(String rrn);
    List<Transaccion> findByStan(String stan);
    List<Transaccion> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
}