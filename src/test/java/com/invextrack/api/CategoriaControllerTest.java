package com.invextrack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.controller.CategoriaController;
import com.invextrack.api.dto.CategoriaRequestDTO;
import com.invextrack.api.model.Categoria;
import com.invextrack.api.repository.CategoriaRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba Unitaria Pura para CategoriaController.
 * Esta versión NO CARGA Spring Security, por lo que no necesitas modificar el pom.xml
 * ni preocuparte por el error 403.
 */
public class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaController categoriaController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Categoria categoriaBase;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks de Mockito
        MockitoAnnotations.openMocks(this);
        
        // Configura MockMvc manualmente para este controlador específico
        // Esto ignora cualquier filtro de seguridad global
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();

        categoriaBase = new Categoria();
        categoriaBase.setId(1);
        categoriaBase.setNombre("Electrónica");
        categoriaBase.setDescripcion("Equipos electrónicos");
    }

    @Test
    void listar_DebeRetornarListaDeCategorias() throws Exception {
        Mockito.when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoriaBase));

        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"));
    }

    @Test
    void crear_DebeRetornar21_CuandoDatosSonValidos() throws Exception {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNombre("Hogar");
        dto.setDescripcion("Artículos de hogar");

        Mockito.when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaBase);

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminar_DebeRetornarOk_CuandoExiste() throws Exception {
        Mockito.when(categoriaRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Categoría eliminada correctamente"));
    }
}