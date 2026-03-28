package com.invextrack.api.service;

import com.invextrack.api.model.Producto;
import com.invextrack.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto guardar(Producto producto) {
        // Ejemplo de estándar: Validar que el stock no sea negativo
        if (producto.getCantidad() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        return repository.save(producto);
    }
}