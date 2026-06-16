package com.eglobal.simulador_transacciones.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class AutorizacionService {

    public String[] simularAutorizacion() {
        Random random = new Random();

        // 70% de probabilidad de aprobado
        boolean aprobado = random.nextInt(100) < 70;

        String status = aprobado ? "00" : "01";
        String autorizacion = aprobado ? generarAutorizacion() : null;

        return new String[]{status, autorizacion};
    }

    private String generarAutorizacion() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return sb.toString();
    }
}