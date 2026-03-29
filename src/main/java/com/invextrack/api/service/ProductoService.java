package com.invextrack.api.service;

import com.invextrack.api.model.Producto;
import com.invextrack.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio relacionada con productos.
 * Centraliza validaciones y reglas de negocio antes de persistir datos.
 */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Retorna todos los productos registrados en el sistema.
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Guarda un producto aplicando validaciones de negocio.
     * Regla: el stock no puede ser un valor negativo.
     */
    public Producto guardar(Producto producto) {
        if (producto.getCantidad() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        return productoRepository.save(producto);
    }
}
