package com.invextrack.api.repository;

import com.invextrack.api.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la persistencia de la tabla
 * movimientos_inventario.
 */
@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoInventario, Integer> {
}