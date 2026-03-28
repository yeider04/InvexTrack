package com.invextrack.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto") // Coincide con PK en SQL
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "sku", nullable = false)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "id_categoria") // Relación con tabla categorías
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor") // Relación con tabla proveedores
    private Proveedor proveedor;
}