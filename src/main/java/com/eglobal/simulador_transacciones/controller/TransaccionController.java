package com.eglobal.simulador_transacciones.controller;

import com.eglobal.simulador_transacciones.dto.TransaccionRequestDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionResponseDTO;
import com.eglobal.simulador_transacciones.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> procesar(@RequestBody @Valid TransaccionRequestDTO request) {
        return ResponseEntity.ok(transaccionService.procesar(request));
    }
}