package com.invextrack.api.controller;

import com.invextrack.api.model.Producto;
import com.invextrack.api.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Gestión del inventario de InvexTrack")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    @Operation(summary = "Obtener lista completa", description = "Retorna todos los productos")
    public List<Producto> listar() {
        return service.listarTodos();
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Añade un nuevo producto al inventario")
    public Producto crear(@RequestBody Producto producto) {
        return service.guardar(producto);
    }
}