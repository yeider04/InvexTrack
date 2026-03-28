package com.invextrack.api.repository;

import com.invextrack.api.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la persistencia de la tabla proveedores.
 * Proporciona métodos CRUD automáticos gracias a JpaRepository.
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}