package com.eglobal.simulador_transacciones.controller;

import com.eglobal.simulador_transacciones.dto.LoginRequestDTO;
import com.eglobal.simulador_transacciones.dto.LoginResponseDTO;
import com.eglobal.simulador_transacciones.model.Usuario;
import com.eglobal.simulador_transacciones.service.JwtService;
import com.eglobal.simulador_transacciones.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody @Valid Usuario usuario) {
        return ResponseEntity.ok(usuarioService.register(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        Usuario usuario = usuarioService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas")); //Autenticación - podrían encontrar un usuario a lo pendejo

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRole());

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                "Bearer",
                usuario.getUsername(),
                usuario.getRole()
        ));
    }
}