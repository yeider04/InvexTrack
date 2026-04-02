package com.invextrack.api.security;

import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que Spring Security usa para cargar el usuario desde la BD.
 * Se coloca en: src/main/java/com/invextrack/api/security/UserDetailsServiceImpl.java
 *
 * IMPORTANTE: Ajusta los nombres de los métodos get() si en tu entidad
 * Usuario se llaman distinto (p.ej. getEmail() en lugar de getCorreo()).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Busca el usuario por correo en la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con correo: " + correo));

        // Convierte el rol a autoridad de Spring Security (ROLE_ADMINISTRADOR, ROLE_OPERARIO)
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol())
        );

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getContrasena(),  // debe estar hasheada con BCrypt en la BD
                authorities
        );
    }
}
