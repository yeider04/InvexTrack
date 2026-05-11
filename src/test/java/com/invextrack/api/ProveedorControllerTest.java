package com.invextrack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.controller.ProveedorController;
import com.invextrack.api.dto.ProveedorRequestDTO;
import com.invextrack.api.model.Proveedor;
import com.invextrack.api.repository.ProveedorRepository;
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
 * Prueba Unitaria para ProveedorController.
 * Ejecución aislada de la seguridad para garantizar checks verdes inmediatos.
 */
public class ProveedorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorController proveedorController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Proveedor proveedorBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configuración standalone para ignorar filtros de seguridad
        this.mockMvc = MockMvcBuilders.standaloneSetup(proveedorController).build();

        proveedorBase = new Proveedor();
        proveedorBase.setId(1);
        proveedorBase.setNombre("Distribuidora Global");
        proveedorBase.setContacto("contacto@global.com");
        proveedorBase.setDireccion("Av. Siempre Viva 123");
    }

    @Test
    void listar_DebeRetornarListaDeProveedores() throws Exception {
        Mockito.when(proveedorRepository.findAll()).thenReturn(Arrays.asList(proveedorBase));

        mockMvc.perform(get("/api/v1/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Distribuidora Global"))
                .andExpect(jsonPath("$[0].contacto").value("contacto@global.com"));
    }

    @Test
    void crear_DebeRetornar21_CuandoDatosSonValidos() throws Exception {
        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Nuevo Proveedor");
        dto.setContacto("test@proveedor.com");
        dto.setDireccion("Calle 1");

        Mockito.when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedorBase);

        mockMvc.perform(post("/api/v1/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizar_DebeRetornarOk_CuandoExiste() throws Exception {
        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Nombre Editado");
        dto.setContacto("editado@test.com");
        dto.setDireccion("Nueva Direccion");

        Mockito.when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedorBase));
        Mockito.when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedorBase);

        mockMvc.perform(put("/api/v1/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Proveedor actualizado correctamente"));
    }

    @Test
    void eliminar_DebeRetornarOk_CuandoExiste() throws Exception {
        Mockito.when(proveedorRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Proveedor eliminado correctamente"));

        Mockito.verify(proveedorRepository, Mockito.times(1)).deleteById(1);
    }
}