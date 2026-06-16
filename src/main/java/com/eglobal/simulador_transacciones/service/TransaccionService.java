package com.eglobal.simulador_transacciones.service;

import com.eglobal.simulador_transacciones.dto.TransaccionRequestDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionResponseDTO;

public interface TransaccionService {
    TransaccionResponseDTO procesar(TransaccionRequestDTO request);
}