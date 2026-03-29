package com.invextrack.api.controller;

import com.invextrack.api.dto.ProveedorRequestDTO;
import com.invextrack.api.model.Proveedor;
import com.invextrack.api.repository.ProveedorRepository;

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
 * Controlador REST para la gestión de proveedores del sistema.
 * Expone endpoints CRUD: Crear, Consultar, Actualizar y Eliminar proveedores.
 */
@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedores", description = "Gestión de proveedores de productos para el inventario")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    /**
     * Retorna todos los proveedores registrados.
     */
    @GetMapping
    @Operation(summary = "Listar todos los proveedores", description = "Retorna la lista completa de proveedores registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay proveedores registrados")
    })
    public ResponseEntity<?> listar() {
        List<Proveedor> listaProveedores = proveedorRepository.findAll();
        if (listaProveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaProveedores);
    }

    /**
     * Busca un proveedor específico por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar proveedor por ID", description = "Retorna un proveedor específico según su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
        @ApiResponse(responseCode = "400", description = "Proveedor no encontrado")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID del proveedor", example = "1")
            @PathVariable Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        return ResponseEntity.ok(proveedor);
    }

    /**
     * Registra un nuevo proveedor en el sistema.
     */
    @PostMapping
    @Operation(summary = "Crear nuevo proveedor", description = "Registra un nuevo proveedor con su información de contacto y dirección")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody ProveedorRequestDTO datosProveedor) {
        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setNombre(datosProveedor.getNombre());
        nuevoProveedor.setContacto(datosProveedor.getContacto());
        nuevoProveedor.setDireccion(datosProveedor.getDireccion());

        Proveedor proveedorGuardado = proveedorRepository.save(nuevoProveedor);
        return ResponseEntity.status(201).body(proveedorGuardado);
    }

    /**
     * Actualiza los datos de un proveedor existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Modifica la información de un proveedor existente identificado por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Proveedor no encontrado o datos inválidos")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del proveedor a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ProveedorRequestDTO datosActualizados) {

        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        proveedorExistente.setNombre(datosActualizados.getNombre());
        proveedorExistente.setContacto(datosActualizados.getContacto());
        proveedorExistente.setDireccion(datosActualizados.getDireccion());

        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);
        return ResponseEntity.ok(Map.of("mensaje", "Proveedor actualizado correctamente", "data", proveedorActualizado));
    }

    /**
     * Elimina un proveedor del sistema.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema. Los productos asociados quedarán sin proveedor.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "Proveedor no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del proveedor a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
        proveedorRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Proveedor eliminado correctamente"));
    }
}
