package com.invextrack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.dto.LoginRequest;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import com.invextrack.api.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
// CAMBIO AQUÍ: Nueva importación para MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoBean; 
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // CAMBIO AQUÍ: Reemplazamos @MockBean por @MockitoBean
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1); 
        usuarioPrueba.setNombre("Juan Perez");
        usuarioPrueba.setCorreo("juan@example.com");
        usuarioPrueba.setContrasena("password123");
        usuarioPrueba.setRol("USER");
    }

    @Test
    void login_DebeRetornarOk_CuandoCredencialesSonValidas() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("juan@example.com", "password123");
        Authentication auth = Mockito.mock(Authentication.class);
        
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        Mockito.when(jwtUtils.generarToken(auth)).thenReturn("jwt-token-falso");
        Mockito.when(usuarioRepository.findByCorreo("juan@example.com")).thenReturn(Optional.of(usuarioPrueba));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-falso"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void registro_DebeRetornarOk_CuandoUsuarioEsNuevo() throws Exception {
        // GIVEN
        Mockito.when(usuarioRepository.findByCorreo(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado correctamente."));
        
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(any(Usuario.class));
    }

    @Test
    void registro_DebeRetornarBadRequest_CuandoCorreoYaExiste() throws Exception {
        // GIVEN
        Mockito.when(usuarioRepository.findByCorreo("juan@example.com")).thenReturn(Optional.of(usuarioPrueba));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El correo ya está registrado."));
        
        Mockito.verify(usuarioRepository, Mockito.never()).save(any(Usuario.class));
    }
}