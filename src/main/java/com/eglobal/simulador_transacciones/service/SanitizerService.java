package com.eglobal.simulador_transacciones.service;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Service;

@Service
public class SanitizerService {

    private final PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    public String sanitize(String input) {
        if (input == null) return null;
        // Elimina HTML y caracteres maliciosos
        String sanitized = policy.sanitize(input);
        // Elimina espacios al inicio y al final
        sanitized = sanitized.trim();
        // Elimina caracteres especiales peligrosos
        sanitized = sanitized.replaceAll("[<>\"'%;()&+]", "");
        return sanitized;
    }
}