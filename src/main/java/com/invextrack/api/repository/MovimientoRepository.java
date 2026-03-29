package com.invextrack.api.repository;

import com.invextrack.api.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para operaciones de persistencia de movimientos de inventario.
 */
@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoInventario, Integer> {
}
