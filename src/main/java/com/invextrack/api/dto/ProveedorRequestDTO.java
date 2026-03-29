package com.invextrack.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para creación y actualización de proveedores.
 */
@Data
@Schema(description = "Datos para crear o actualizar un proveedor")
public class ProveedorRequestDTO {

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Schema(description = "Nombre o razón social del proveedor", example = "TechDistrib S.A.")
    private String nombre;

    @Schema(description = "Número de teléfono o nombre del contacto", example = "3001234567")
    private String contacto;

    @Schema(description = "Dirección física del proveedor", example = "Calle 10 # 5-20, Bogotá")
    private String direccion;
}
