package com.invextrack.api.dto;

/**
 * DTO para recibir las credenciales del usuario en el login.
 * Se coloca en: src/main/java/com/invextrack/api/dto/LoginRequest.java
 */
public class LoginRequest {

    private String correo;
    private String contrasena;

    public LoginRequest() {}

    public LoginRequest(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
