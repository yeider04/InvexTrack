package com.invextrack.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que se ejecuta UNA VEZ por cada petición HTTP.
 * Lee el token JWT del encabezado Authorization y autentica al usuario.
 * Se coloca en: src/main/java/com/invextrack/api/security/JwtAuthFilter.java
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Extrae el token del encabezado "Authorization: Bearer <token>"
            String jwt = extraerToken(request);

            // 2. Si hay token y es válido, autentica al usuario en el contexto
            if (jwt != null && jwtUtils.validarToken(jwt)) {
                String correo = jwtUtils.getCorreoDesdeToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            System.err.println("No se puede autenticar el usuario: " + e.getMessage());
        }

        // 3. Continúa la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // ── Extrae el token quitando el prefijo "Bearer " ────────────────────────
    private String extraerToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
