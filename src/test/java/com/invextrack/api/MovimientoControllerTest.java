package com.invextrack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.controller.MovimientoController;
import com.invextrack.api.dto.MovimientoRequestDTO;
import com.invextrack.api.model.MovimientoInventario;
import com.invextrack.api.model.Producto;
import com.invextrack.api.model.Usuario;
import com.invextrack.api.repository.MovimientoRepository;
import com.invextrack.api.repository.ProductoRepository;
import com.invextrack.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba Unitaria para MovimientoController.
 * Configuración Standalone para omitir filtros de seguridad y dependencias externas.
 */
public class MovimientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MovimientoController movimientoController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private MovimientoInventario movimientoBase;
    private Producto productoBase;
    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movimientoController).build();

        // Datos de prueba
        productoBase = new Producto();
        productoBase.setId(1);
        productoBase.setNombre("Producto Test");

        usuarioBase = new Usuario();
        usuarioBase.setId(1);
        usuarioBase.setNombre("Usuario Test");

        movimientoBase = new MovimientoInventario();
        movimientoBase.setId(1);
        movimientoBase.setTipo("ENTRADA");
        movimientoBase.setCantidad(50);
        movimientoBase.setProducto(productoBase);
        movimientoBase.setUsuario(usuarioBase);
    }

    @Test
    void listar_DebeRetornarListaDeMovimientos() throws Exception {
        Mockito.when(movimientoRepository.findAll()).thenReturn(Arrays.asList(movimientoBase));

        mockMvc.perform(get("/api/v1/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$[0].cantidad").value(50));
    }

    @Test
    void crear_DebeRetornar21_CuandoDatosSonValidos() throws Exception {
        MovimientoRequestDTO dto = new MovimientoRequestDTO();
        dto.setTipo("ENTRADA");
        dto.setCantidad(10);
        dto.setIdProducto(1);
        dto.setIdUsuario(1);

        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(productoBase));
        Mockito.when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioBase));
        Mockito.when(movimientoRepository.save(any(MovimientoInventario.class))).thenReturn(movimientoBase);

        mockMvc.perform(post("/api/v1/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void buscarPorId_DebeRetornarMovimiento_CuandoExiste() throws Exception {
        Mockito.when(movimientoRepository.findById(1)).thenReturn(Optional.of(movimientoBase));

        mockMvc.perform(get("/api/v1/movimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_DebeRetornarOk_CuandoExiste() throws Exception {
        Mockito.when(movimientoRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/movimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Movimiento eliminado correctamente"));
    }
}