package com.eglobal.simulador_transacciones.service;

import com.eglobal.simulador_transacciones.dto.TransaccionHistorialDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionRequestDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionResponseDTO;
import com.eglobal.simulador_transacciones.model.Transaccion;
import com.eglobal.simulador_transacciones.model.Usuario;
import com.eglobal.simulador_transacciones.repository.TransaccionRepository;
import com.eglobal.simulador_transacciones.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestTemplate restTemplate;
    private final SanitizerService sanitizerService;

    @Value("${internal.api.secret}")
    private String internalSecret;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                  UsuarioRepository usuarioRepository,
                                  SanitizerService sanitizerService) {
        this.transaccionRepository = transaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.sanitizerService = sanitizerService;
        this.restTemplate = new RestTemplate();
    }

    // Orquesta el flujo completo de una transacción: sanitiza los campos, llama al autorizador interno (API 2)
    // con el header secreto, persiste el resultado en BD y devuelve la respuesta al controller
    @Override
    public TransaccionResponseDTO procesar(TransaccionRequestDTO request, String username) {
        // Buscar el usuario autenticado para asociarlo a la transacción
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        String rrn = sanitizerService.sanitize(request.getRrn());
        String stan = sanitizerService.sanitize(request.getStan());
        String fecha = sanitizerService.sanitize(request.getFecha());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Secret", internalSecret);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:8080/api/autorizacion",
                HttpMethod.POST,
                entity,
                Map.class
        );

        String status = (String) response.getBody().get("status");
        String autorizacion = (String) response.getBody().get("autorizacion");

        Transaccion transaccion = new Transaccion();
        transaccion.setRrn(rrn);
        transaccion.setStan(stan);
        transaccion.setImporte(request.getImporte());
        transaccion.setFecha(fecha);
        transaccion.setStatusRespuesta(status);
        transaccion.setAutorizacion(autorizacion);
        transaccion.setCreatedAt(LocalDateTime.now());
        transaccion.setUsuario(usuario);
        transaccionRepository.save(transaccion);

        String mensaje = status.equals("00") ? "Aprobado" : "Declinado";
        return new TransaccionResponseDTO(
                rrn,
                stan,
                status,
                autorizacion,
                mensaje
        );
    }

    // Devuelve el historial de transacciones del usuario autenticado, ordenado por fecha descendente
    @Override
    public List<TransaccionHistorialDTO> obtenerHistorial(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Transaccion> transacciones = transaccionRepository
                .findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());

        return transacciones.stream()
                .map(t -> new TransaccionHistorialDTO(
                        t.getId(),
                        t.getRrn(),
                        t.getStan(),
                        t.getImporte(),
                        t.getFecha(),
                        t.getStatusRespuesta(),
                        t.getAutorizacion(),
                        t.getCreatedAt()
                ))
                .toList();
    }
}