package com.invextrack.api.controller;

import com.invextrack.api.model.Categoria;
import com.invextrack.api.model.Producto;
import com.invextrack.api.model.Proveedor;
import com.invextrack.api.repository.CategoriaRepository;
// import com.invextrack.api.service.ProductoService;
import com.invextrack.api.repository.ProductoRepository;
import com.invextrack.api.repository.ProveedorRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CategoriaRepository categoriaRepo; // Necesario para buscar la categoría

    @Autowired
    private ProveedorRepository proveedorRepo; // Necesario para buscar el proveedor

    // 1. CONSULTA (READ)
    @GetMapping
    @Operation(summary = "Lista completa de productos")
    public ResponseEntity<?> listar() {
        List<Producto> lista = productoRepo.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }

        return ResponseEntity.ok(lista);
    }

    // 2. INSERCIÓN (CREATE)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        // 1. Creamos la instancia de Producto
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre((String) payload.get("nombre"));
        nuevoProducto.setDescripcion((String) payload.get("descripcion"));
        nuevoProducto.setSku((String) payload.get("sku"));
        nuevoProducto.setCantidad((Integer) payload.get("cantidad"));
        nuevoProducto.setPrecioUnitario(Double.parseDouble(payload.get("precioUnitario").toString()));
        nuevoProducto.setPrecio(Double.parseDouble(payload.get("precio").toString()));

        // 2. Buscamos y asignamos la Categoría REAL
        Integer idCat = (Integer) payload.get("idCategoria");
        Categoria cat = categoriaRepo.findById(idCat)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        nuevoProducto.setCategoria(cat);

        // 3. Buscamos y asignamos el Proveedor REAL
        Integer idProv = (Integer) payload.get("idProveedor");
        Proveedor prov = proveedorRepo.findById(idProv)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        nuevoProducto.setProveedor(prov);

        // 4. Guardamos
        Producto guardado = productoRepo.save(nuevoProducto);

        return ResponseEntity.status(201).body(guardado);
    }

    // 3. ACTUALIZACIÓN (UPDATE)
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        // 1. Buscar si el producto existe
        Producto productoExistente = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // 2. Actualizar datos básicos
        productoExistente.setNombre((String) payload.get("nombre"));
        productoExistente.setDescripcion((String) payload.get("descripcion"));
        productoExistente.setSku((String) payload.get("sku"));
        productoExistente.setCantidad((Integer) payload.get("cantidad"));
        productoExistente.setPrecioUnitario(Double.parseDouble(payload.get("precioUnitario").toString()));
        productoExistente.setPrecio(Double.parseDouble(payload.get("precio").toString()));

        // 3. Actualizar Relación: Categoría
        Integer idCat = (Integer) payload.get("idCategoria");
        Categoria cat = categoriaRepo.findById(idCat)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        productoExistente.setCategoria(cat);

        // 4. Actualizar Relación: Proveedor
        Integer idProv = (Integer) payload.get("idProveedor");
        Proveedor prov = proveedorRepo.findById(idProv)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        productoExistente.setProveedor(prov);

        // 5. Guardar cambios
        Producto actualizado = productoRepo.save(productoExistente);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto actualizado correctamente");
        respuesta.put("data", actualizado);

        return ResponseEntity.ok(respuesta);
    }

    // 4. ELIMINACIÓN (DELETE)
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto del sistema")
    public void eliminar(@PathVariable Integer id) {
        productoRepo.deleteById(id);
    }
    
}