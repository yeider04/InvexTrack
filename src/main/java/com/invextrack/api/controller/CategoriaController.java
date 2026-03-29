package com.invextrack.api.controller;

import com.invextrack.api.dto.CategoriaRequestDTO;
import com.invextrack.api.model.Categoria;
import com.invextrack.api.repository.CategoriaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de categorías de productos.
 * Expone endpoints CRUD: Crear, Consultar, Actualizar y Eliminar categorías.
 */
@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías para clasificar los productos del inventario")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Retorna todas las categorías registradas.
     */
    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Retorna la lista completa de categorías disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay categorías registradas")
    })
    public ResponseEntity<?> listar() {
        List<Categoria> listaCategorias = categoriaRepository.findAll();
        if (listaCategorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaCategorias);
    }

    /**
     * Busca una categoría específica por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar categoría por ID", description = "Retorna una categoría específica según su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "400", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return ResponseEntity.ok(categoria);
    }

    /**
     * Registra una nueva categoría en el sistema.
     */
    @PostMapping
    @Operation(summary = "Crear nueva categoría", description = "Registra una nueva categoría para clasificar productos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody CategoriaRequestDTO datosCategoria) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(datosCategoria.getNombre());
        nuevaCategoria.setDescripcion(datosCategoria.getDescripcion());

        Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
        return ResponseEntity.status(201).body(categoriaGuardada);
    }

    /**
     * Actualiza los datos de una categoría existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Modifica el nombre y descripción de una categoría existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Categoría no encontrada o datos inválidos")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la categoría a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO datosActualizados) {

        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoriaExistente.setNombre(datosActualizados.getNombre());
        categoriaExistente.setDescripcion(datosActualizados.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return ResponseEntity.ok(Map.of("mensaje", "Categoría actualizada correctamente", "data", categoriaActualizada));
    }

    /**
     * Elimina una categoría del sistema.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema. Los productos asociados quedarán sin categoría.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "400", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la categoría a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Categoría eliminada correctamente"));
    }
}
