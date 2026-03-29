package com.invextrack.api.repository;

import com.invextrack.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método extra para buscar por correo (útil para validaciones)
    Optional<Usuario> findByCorreo(String correo);
}