package com.invextrack.api.controller;

import com.invextrack.api.dto.ProductoRequestDTO;
import com.invextrack.api.model.Categoria;
import com.invextrack.api.model.Producto;
import com.invextrack.api.model.Proveedor;
import com.invextrack.api.repository.CategoriaRepository;
import com.invextrack.api.repository.ProductoRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de productos del inventario.
 * Expone endpoints CRUD: Crear, Consultar, Actualizar y Eliminar productos.
 */
@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para la gestión de productos en el inventario")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    /**
     * Retorna la lista completa de productos registrados en el sistema.
     */
    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Retorna la lista completa de productos con sus categorías y proveedores asociados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay productos registrados")
    })
    public ResponseEntity<?> listar() {
        List<Producto> listaProductos = productoRepository.findAll();
        if (listaProductos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaProductos);
    }

    /**
     * Busca y retorna un producto específico por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto por ID", description = "Retorna un producto específico según su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "400", description = "Producto no encontrado con el ID proporcionado")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID único del producto", example = "1")
            @PathVariable Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return ResponseEntity.ok(producto);
    }

    /**
     * Registra un nuevo producto en el sistema.
     */
    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Registra un nuevo producto en el inventario. Requiere IDs válidos de categoría y proveedor existentes.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o categoría/proveedor no encontrados")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody ProductoRequestDTO datosProducto) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(datosProducto.getNombre());
        nuevoProducto.setDescripcion(datosProducto.getDescripcion());
        nuevoProducto.setSku(datosProducto.getSku());
        nuevoProducto.setCantidad(datosProducto.getCantidad());
        nuevoProducto.setPrecioUnitario(datosProducto.getPrecioUnitario());
        nuevoProducto.setPrecio(datosProducto.getPrecio());

        Categoria categoriaAsociada = categoriaRepository.findById(datosProducto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + datosProducto.getIdCategoria()));
        nuevoProducto.setCategoria(categoriaAsociada);

        Proveedor proveedorAsociado = proveedorRepository.findById(datosProducto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + datosProducto.getIdProveedor()));
        nuevoProducto.setProveedor(proveedorAsociado);

        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return ResponseEntity.status(201).body(productoGuardado);
    }

    /**
     * Actualiza los datos de un producto existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente", description = "Modifica todos los campos de un producto identificado por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Producto, categoría o proveedor no encontrado")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO datosActualizados) {

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        productoExistente.setNombre(datosActualizados.getNombre());
        productoExistente.setDescripcion(datosActualizados.getDescripcion());
        productoExistente.setSku(datosActualizados.getSku());
        productoExistente.setCantidad(datosActualizados.getCantidad());
        productoExistente.setPrecioUnitario(datosActualizados.getPrecioUnitario());
        productoExistente.setPrecio(datosActualizados.getPrecio());

        Categoria categoriaAsociada = categoriaRepository.findById(datosActualizados.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + datosActualizados.getIdCategoria()));
        productoExistente.setCategoria(categoriaAsociada);

        Proveedor proveedorAsociado = proveedorRepository.findById(datosActualizados.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + datosActualizados.getIdProveedor()));
        productoExistente.setProveedor(proveedorAsociado);

        Producto productoActualizado = productoRepository.save(productoExistente);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto actualizado correctamente");
        respuesta.put("data", productoActualizado);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Elimina un producto del sistema por su ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina de forma permanente un producto del inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "Producto no encontrado con el ID proporcionado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
    }
}
