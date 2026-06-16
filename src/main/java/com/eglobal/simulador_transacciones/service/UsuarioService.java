package com.eglobal.simulador_transacciones.service;

import com.eglobal.simulador_transacciones.model.Usuario;
import java.util.Optional;

public interface UsuarioService {
    Usuario register(Usuario usuario);
    Optional<Usuario> findByUsername(String username);
}