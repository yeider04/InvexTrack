package com.invextrack.api.controller;

import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// import java.util.Optional;

/**
 * REEMPLAZA tu UsuarioController.java actual con este.
 *
 * Cambios respecto al original:
 *  - create(): hashea la contraseña con BCrypt antes de guardar.
 *  - update(): si viene una contraseña nueva, también la hashea.
 *    Si el campo contraseña llega vacío en el PUT, conserva la anterior.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // ← inyecta el BCrypt de SecurityConfig

    // ── GET todos ────────────────────────────────────────────────────────────
    @GetMapping
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    // ── GET por id ────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST crear usuario ────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        // CORRECCIÓN: hashear la contraseña antes de guardar
        if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // ── PUT actualizar usuario ────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id,
                                          @RequestBody Usuario datos) {
        return usuarioRepository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setCorreo(datos.getCorreo());
            existente.setRol(datos.getRol());

            // CORRECCIÓN: solo hashea si viene una contraseña nueva no vacía
            // Si el campo llega vacío, conserva la contraseña actual
            if (datos.getContrasena() != null && !datos.getContrasena().isBlank()) {
                existente.setContrasena(passwordEncoder.encode(datos.getContrasena()));
            }

            return ResponseEntity.ok(usuarioRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
