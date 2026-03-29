package com.invextrack.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para creación y actualización de categorías.
 */
@Data
@Schema(description = "Datos para crear o actualizar una categoría")
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Productos electrónicos y accesorios tecnológicos")
    private String descripcion;
}
