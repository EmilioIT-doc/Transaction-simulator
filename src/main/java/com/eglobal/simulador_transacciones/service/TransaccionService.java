package com.eglobal.simulador_transacciones.service;

import com.eglobal.simulador_transacciones.dto.TransaccionHistorialDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionRequestDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionResponseDTO;
import java.util.List;

public interface TransaccionService {
    TransaccionResponseDTO procesar(TransaccionRequestDTO request, String username);
    List<TransaccionHistorialDTO> obtenerHistorial(String username);
}