package com.invextrack.api.controller;

import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de personal del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios registrados")
    public ResponseEntity<?> listar() {
        List<Usuario> lista = usuarioRepo.findAll();
        Map<String, Object> res = new HashMap<>();
        res.put("mensaje", "Usuarios recuperados");
        res.put("total", lista.size());
        res.put("data", lista);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        Usuario guardado = usuarioRepo.save(usuario);
        return ResponseEntity.status(201).body(guardado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario por ID")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        usuarioRepo.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Usuario eliminado correctamente"));
    }
}