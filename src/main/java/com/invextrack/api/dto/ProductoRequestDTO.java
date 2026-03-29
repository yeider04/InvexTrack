package com.invextrack.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para recibir datos de creación y actualización de productos.
 * Permite que Swagger UI muestre un formulario claro con ejemplos.
 */
@Data
@Schema(description = "Datos requeridos para crear o actualizar un producto")
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Schema(description = "Nombre del producto", example = "Teclado Mecánico RGB")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Teclado mecánico con switches azules y retroiluminación RGB")
    private String descripcion;

    @NotBlank(message = "El SKU es obligatorio")
    @Schema(description = "Código único de identificación del producto (SKU)", example = "TEC-RGB-001")
    private String sku;

    @NotNull(message = "La cantidad en stock es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Schema(description = "Cantidad disponible en inventario", example = "50")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    @Schema(description = "Precio de compra (costo unitario)", example = "85000.00")
    private Double precioUnitario;

    @NotNull(message = "El precio de venta es obligatorio")
    @Min(value = 0, message = "El precio de venta no puede ser negativo")
    @Schema(description = "Precio de venta al público", example = "120000.00")
    private Double precio;

    @NotNull(message = "El ID de categoría es obligatorio")
    @Schema(description = "ID de la categoría a la que pertenece el producto", example = "1")
    private Integer idCategoria;

    @NotNull(message = "El ID de proveedor es obligatorio")
    @Schema(description = "ID del proveedor del producto", example = "1")
    private Integer idProveedor;
}
