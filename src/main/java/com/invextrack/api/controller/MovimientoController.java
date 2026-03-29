package com.invextrack.api.controller;

import com.invextrack.api.dto.MovimientoRequestDTO;
import com.invextrack.api.model.MovimientoInventario;
import com.invextrack.api.model.Producto;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.MovimientoRepository;
import com.invextrack.api.repository.ProductoRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de movimientos de inventario.
 * Registra entradas y salidas de productos del almacén.
 */
@RestController
@RequestMapping("/api/v1/movimientos")
@Tag(name = "Movimientos de Inventario", description = "Registro de entradas y salidas de productos en el almacén")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retorna todos los movimientos registrados.
     */
    @GetMapping
    @Operation(summary = "Listar todos los movimientos", description = "Retorna el historial completo de entradas y salidas del inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay movimientos registrados")
    })
    public ResponseEntity<?> listar() {
        List<MovimientoInventario> listaMovimientos = movimientoRepository.findAll();
        if (listaMovimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaMovimientos);
    }

    /**
     * Busca un movimiento específico por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar movimiento por ID", description = "Retorna el detalle de un movimiento específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
        @ApiResponse(responseCode = "400", description = "Movimiento no encontrado")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID del movimiento", example = "1")
            @PathVariable Integer id) {
        MovimientoInventario movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));
        return ResponseEntity.ok(movimiento);
    }

    /**
     * Registra un nuevo movimiento de inventario (ENTRADA o SALIDA).
     */
    @PostMapping
    @Operation(summary = "Registrar movimiento de inventario",
               description = "Registra una ENTRADA o SALIDA de productos. La fecha se asigna automáticamente al día actual.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o producto/usuario no encontrado")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody MovimientoRequestDTO datosMovimiento) {
        Producto productoAsociado = productoRepository.findById(datosMovimiento.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + datosMovimiento.getIdProducto()));

        Usuario usuarioAsociado = usuarioRepository.findById(datosMovimiento.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + datosMovimiento.getIdUsuario()));

        MovimientoInventario nuevoMovimiento = new MovimientoInventario();
        nuevoMovimiento.setTipo(datosMovimiento.getTipo().toUpperCase());
        nuevoMovimiento.setCantidad(datosMovimiento.getCantidad());
        nuevoMovimiento.setFecha(LocalDate.now());
        nuevoMovimiento.setProducto(productoAsociado);
        nuevoMovimiento.setUsuario(usuarioAsociado);

        MovimientoInventario movimientoGuardado = movimientoRepository.save(nuevoMovimiento);
        return ResponseEntity.status(201).body(movimientoGuardado);
    }

    /**
     * Elimina un movimiento del historial.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento", description = "Elimina un registro de movimiento del historial de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Movimiento eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "Movimiento no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del movimiento a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!movimientoRepository.existsById(id)) {
            throw new RuntimeException("Movimiento no encontrado con ID: " + id);
        }
        movimientoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Movimiento eliminado correctamente"));
    }
}
