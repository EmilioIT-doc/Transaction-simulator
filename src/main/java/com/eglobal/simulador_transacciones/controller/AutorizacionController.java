package com.eglobal.simulador_transacciones.controller;

import com.eglobal.simulador_transacciones.service.AutorizacionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/autorizacion")
public class AutorizacionController {

    private final AutorizacionService autorizacionService;

    @Value("${internal.api.secret}")
    private String internalSecret;

    public AutorizacionController(AutorizacionService autorizacionService) {
        this.autorizacionService = autorizacionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> autorizar(
            @RequestHeader(value = "X-Internal-Secret", required = false) String secret) {

        if (secret == null || !secret.equals(internalSecret)) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Acceso no autorizado a API interna");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        String[] resultado = autorizacionService.simularAutorizacion();
        Map<String, String> response = new HashMap<>();
        response.put("status", resultado[0]);
        response.put("autorizacion", resultado[1] != null ? resultado[1] : "");
        return ResponseEntity.ok(response);
    }
}