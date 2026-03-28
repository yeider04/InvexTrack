package com.invextrack.api.controller;

import com.invextrack.api.model.*;
import com.invextrack.api.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GestionController {

    @Autowired
    private UsuarioRepository userRepo;
    @Autowired
    private CategoriaRepository catRepo;

    @Tag(name = "Usuarios", description = "Gestión de personal")
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios")
    public List<Usuario> listarUsuarios() {
        return userRepo.findAll();
    }

    @Tag(name = "Categorias", description = "Categorización de inventario")
    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías")
    public List<Categoria> listarCategorias() {
        return catRepo.findAll();
    }

    @Autowired
    private ProveedorRepository provRepo;
    @Autowired
    private MovimientoRepository movRepo;

    @Tag(name = "Proveedores", description = "Directorio de proveedores de InvexTrack")
    @GetMapping("/proveedores")
    @Operation(summary = "Obtener todos los proveedores")
    public List<Proveedor> listarProveedores() {
        return provRepo.findAll();
    }

    @Tag(name = "Movimientos", description = "Historial de entradas y salidas")
    @GetMapping("/movimientos")
    @Operation(summary = "Consultar historial de movimientos")
    public List<MovimientoInventario> listarMovimientos() {
        return movRepo.findAll();
    }
}