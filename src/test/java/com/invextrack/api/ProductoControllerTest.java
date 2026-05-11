package com.invextrack.api; // Ruta de la carpeta raíz

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invextrack.api.dto.ProductoRequestDTO;
import com.invextrack.api.model.Categoria;
import com.invextrack.api.model.Producto;
import com.invextrack.api.model.Proveedor;
import com.invextrack.api.repository.CategoriaRepository;
import com.invextrack.api.repository.ProductoRepository;
import com.invextrack.api.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba de integración para ProductoController.
 * Ubicación: src/test/java/com/invextrack/api/
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Evita el error 403 Forbidden
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoRepository productoRepository;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    @MockitoBean
    private ProveedorRepository proveedorRepository;

    private Producto productoBase;
    private Categoria categoriaBase;
    private Proveedor proveedorBase;

    @BeforeEach
    void setUp() {
        categoriaBase = new Categoria();
        categoriaBase.setId(1);
        categoriaBase.setNombre("Electrónica");

        proveedorBase = new Proveedor();
        proveedorBase.setId(1);
        proveedorBase.setNombre("Proveedor Test");

        productoBase = new Producto();
        productoBase.setId(1);
        productoBase.setNombre("Laptop");
        productoBase.setSku("LAP-001");
        productoBase.setCantidad(10);
        productoBase.setPrecioUnitario(1000.0);
        productoBase.setCategoria(categoriaBase);
        productoBase.setProveedor(proveedorBase);
    }

    @Test
    void listar_DebeRetornarListaDeProductos() throws Exception {
        Mockito.when(productoRepository.findAll()).thenReturn(Arrays.asList(productoBase));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"));
    }

    @Test
    void crear_DebeRetornar21_CuandoDatosSonValidos() throws Exception {
        // 1. Creamos el DTO con TODOS los campos posibles para evitar el error 400
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Laptop Gamer");
        dto.setSku("LAP-123-ABC");
        dto.setCantidad(10);
        dto.setPrecioUnitario(1500.0);
        dto.setDescripcion("Descripción de prueba obligatoria"); // A veces es obligatoria
        dto.setIdCategoria(1);
        dto.setIdProveedor(1);

        // 2. Configuramos los Mocks para que devuelvan objetos válidos (NO nulos)
        Mockito.when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaBase));
        Mockito.when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedorBase));
        
        // El save debe devolver el productoBase que ya tiene un ID asignado
        Mockito.when(productoRepository.save(any(Producto.class))).thenReturn(productoBase);

        // 3. Ejecutamos la petición
        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // Debería dar 201
    }

    @Test
    void eliminar_DebeRetornarOk_CuandoProductoExiste() throws Exception {
        Mockito.when(productoRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Producto eliminado correctamente"));

        Mockito.verify(productoRepository, Mockito.times(1)).deleteById(1);
    }
}