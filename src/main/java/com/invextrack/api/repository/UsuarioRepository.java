package com.invextrack.api.repository;

import com.invextrack.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AGREGA findByCorreo si no existe en tu UsuarioRepository.
 * El <Usuario, Integer> usa Integer porque tu @Id es Integer.
 * Se coloca en: src/main/java/com/invextrack/api/repository/UsuarioRepository.java
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
}
