package com.invextrack.api.controller;

import com.invextrack.api.dto.LoginRequest;
import com.invextrack.api.dto.LoginResponse;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import com.invextrack.api.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Login y gestión de sesión")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getCorreo(),
                        loginRequest.getContrasena()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generarToken(authentication);

        Usuario usuario = usuarioRepository.findByCorreo(loginRequest.getCorreo())
                .orElseThrow();

        LoginResponse respuesta = new LoginResponse(
                jwt,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol()
        );

        // CORRECCIÓN: ResponseEntity.ok() sin tipo genérico explícito
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<String> registro(@RequestBody Usuario nuevoUsuario) {
        if (usuarioRepository.findByCorreo(nuevoUsuario.getCorreo()).isPresent()) {
            // CORRECCIÓN: badRequest() sin parámetro de tipo genérico
            return ResponseEntity.badRequest().body("El correo ya está registrado.");
        }
        nuevoUsuario.setContrasena(passwordEncoder.encode(nuevoUsuario.getContrasena()));
        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado correctamente.");
    }
}
