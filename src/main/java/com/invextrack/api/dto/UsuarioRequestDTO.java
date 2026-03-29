package com.invextrack.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para creación y actualización de usuarios del sistema.
 */
@Data
@Schema(description = "Datos para registrar o actualizar un usuario")
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre del usuario es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Carlos Pérez")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Schema(description = "Correo electrónico (debe ser único en el sistema)", example = "carlos.perez@invextrack.com")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "miContrasena123")
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario en el sistema. Valores permitidos: ADMINISTRADOR, OPERARIO", example = "OPERARIO")
    private String rol;
}
