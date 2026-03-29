package com.invextrack.api.controller;

import com.invextrack.api.model.Categoria;
import com.invextrack.api.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
import java.util.List;
// import org.springframework.http.ResponseEntity;
// import java.util.Map;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepo;

    // CONSULTAR TODAS
    @GetMapping
    @Operation(summary = "Obtener lista completa de categorías")
    public ResponseEntity<?> listar() {
        List<Categoria> lista = categoriaRepo.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }

        return ResponseEntity.ok(lista);
    }

    // INSERTAR NUEVA
    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    public Categoria crear(@RequestBody Categoria categoria) {
        return categoriaRepo.save(categoria);
    }

    // ACTUALIZAR EXISTENTE
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una categoría")
    public Categoria actualizar(@PathVariable Integer id, @RequestBody Categoria detalles) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setNombre(detalles.getNombre());
        categoria.setDescripcion(detalles.getDescripcion());

        return categoriaRepo.save(categoria);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID")
    public String eliminar(@PathVariable Integer id) {
        categoriaRepo.deleteById(id);
        return "Categoría eliminada con éxito.";
    }
}