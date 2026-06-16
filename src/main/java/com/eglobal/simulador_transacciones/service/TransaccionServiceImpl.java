package com.eglobal.simulador_transacciones.service;

import com.eglobal.simulador_transacciones.dto.TransaccionRequestDTO;
import com.eglobal.simulador_transacciones.dto.TransaccionResponseDTO;
import com.eglobal.simulador_transacciones.model.Transaccion;
import com.eglobal.simulador_transacciones.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final RestTemplate restTemplate;

    @Value("${internal.api.secret}")
    private String internalSecret;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public TransaccionResponseDTO procesar(TransaccionRequestDTO request) {
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
        transaccion.setRrn(request.getRrn());
        transaccion.setStan(request.getStan());
        transaccion.setImporte(request.getImporte());
        transaccion.setFecha(request.getFecha());
        transaccion.setStatusRespuesta(status);
        transaccion.setAutorizacion(autorizacion);
        transaccion.setCreatedAt(LocalDateTime.now());
        transaccionRepository.save(transaccion);

        String mensaje = status.equals("00") ? "Aprobado" : "Declinado";
        return new TransaccionResponseDTO(
                request.getRrn(),
                request.getStan(),
                status,
                autorizacion,
                mensaje
        );
    }
}