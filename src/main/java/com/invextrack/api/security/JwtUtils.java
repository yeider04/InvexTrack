package com.invextrack.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Componente responsable de generar, validar y extraer información de tokens JWT.
 * Se coloca en: src/main/java/com/invextrack/api/security/JwtUtils.java
 */
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // ── Genera el token a partir del correo del usuario ──────────────────────
    public String generarToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Extrae el correo (subject) del token ─────────────────────────────────
    public String getCorreoDesdeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ── Valida que el token sea correcto y no haya expirado ──────────────────
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("JWT malformado: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT vacío: " + e.getMessage());
        }
        return false;
    }

    // ── Construye la clave secreta desde el string en application.yml ────────
    private Key getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
