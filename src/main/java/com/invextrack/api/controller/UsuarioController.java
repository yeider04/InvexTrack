package com.invextrack.api.controller;

import com.invextrack.api.dto.UsuarioRequestDTO;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de usuarios del sistema InvexTrack.
 * Expone endpoints CRUD: Crear, Consultar, Actualizar y Eliminar usuarios.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión del personal con acceso al sistema InvexTrack")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retorna todos los usuarios registrados en el sistema.
     */
    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista completa de usuarios registrados con su rol en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    })
    public ResponseEntity<?> listar() {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuarios recuperados exitosamente");
        respuesta.put("total", listaUsuarios.size());
        respuesta.put("data", listaUsuarios);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Busca un usuario específico por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuario por ID", description = "Retorna un usuario específico según su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID único del usuario", example = "1")
            @PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(usuario);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @PostMapping
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario con rol ADMINISTRADOR u OPERARIO. El correo debe ser único.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o correo ya registrado")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioRequestDTO datosUsuario) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(datosUsuario.getNombre());
        nuevoUsuario.setCorreo(datosUsuario.getCorreo());
        nuevoUsuario.setContrasena(datosUsuario.getContrasena());
        nuevoUsuario.setRol(datosUsuario.getRol());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.status(201).body(usuarioGuardado);
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente identificado por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario no encontrado o datos inválidos")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO datosActualizados) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuarioExistente.setNombre(datosActualizados.getNombre());
        usuarioExistente.setCorreo(datosActualizados.getCorreo());
        usuarioExistente.setContrasena(datosActualizados.getContrasena());
        usuarioExistente.setRol(datosActualizados.getRol());

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario actualizado correctamente", "data", usuarioActualizado));
    }

    /**
     * Elimina un usuario del sistema.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema de forma permanente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
    }
}
