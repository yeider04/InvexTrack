package com.invextrack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.controller.UsuarioController;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba Unitaria para UsuarioController.
 * Valida la lógica de gestión de usuarios y el hasheo de contraseñas.
 */
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();

        usuarioBase = new Usuario();
        usuarioBase.setId(1);
        usuarioBase.setNombre("Juan Perez");
        usuarioBase.setCorreo("juan@example.com");
        usuarioBase.setRol("ADMIN");
        usuarioBase.setContrasena("password123");
    }

    @Test
    void getAll_DebeRetornarListaDeUsuarios() throws Exception {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioBase));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"))
                .andExpect(jsonPath("$[0].correo").value("juan@example.com"));
    }

    @Test
    void create_DebeHashearContrasenaYGuardar() throws Exception {
        // Simulamos que el encoder devuelve un hash
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBase);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioBase)))
                .andExpect(status().isOk());

        // Verificamos que se llamó al encoder para proteger la contraseña
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    }

    @Test
    void getById_DebeRetornarUsuario_CuandoExiste() throws Exception {
        Mockito.when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioBase));

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_DebeActualizarYHashearSiVieneNuevaContrasena() throws Exception {
        Mockito.when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioBase));
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("new_hash");
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBase);

        Usuario datosNuevos = new Usuario();
        datosNuevos.setNombre("Juan Modificado");
        datosNuevos.setContrasena("nuevaContrasena");

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosNuevos)))
                .andExpect(status().isOk());

        Mockito.verify(passwordEncoder).encode("nuevaContrasena");
    }

    @Test
    void delete_DebeRetornarNoContent_CuandoExiste() throws Exception {
        Mockito.when(usuarioRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(usuarioRepository).deleteById(1);
    }
}