package com.invextrack.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para registrar movimientos de inventario (entradas y salidas).
 */
@Data
@Schema(description = "Datos para registrar un movimiento de inventario")
public class MovimientoRequestDTO {

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Schema(description = "Tipo de movimiento. Valores permitidos: ENTRADA, SALIDA", example = "ENTRADA")
    private String tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Schema(description = "Cantidad de unidades del movimiento", example = "10")
    private Integer cantidad;

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto involucrado en el movimiento", example = "1")
    private Integer idProducto;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario que registra el movimiento", example = "1")
    private Integer idUsuario;
}
