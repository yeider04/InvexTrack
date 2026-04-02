package com.invextrack.api.dto;

/**
 * DTO para enviar la respuesta del login exitoso al frontend.
 * CORRECCIÓN: usa Integer para el id (igual que tu entidad Usuario).
 * Se coloca en: src/main/java/com/invextrack/api/dto/LoginResponse.java
 */
public class LoginResponse {

    private String token;
    private String tipo;
    private Integer id;      // ← Integer, no Long (igual que tu @Id en Usuario.java)
    private String nombre;
    private String correo;
    private String rol;

    public LoginResponse() {
        this.tipo = "Bearer";
    }

    public LoginResponse(String token, Integer id, String nombre, String correo, String rol) {
        this.tipo    = "Bearer";
        this.token   = token;
        this.id      = id;
        this.nombre  = nombre;
        this.correo  = correo;
        this.rol     = rol;
    }

    public String getToken()  { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo()   { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getId()    { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol()    { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
