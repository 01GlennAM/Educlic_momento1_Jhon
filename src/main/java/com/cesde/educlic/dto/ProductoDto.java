package com.cesde.educlic.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductoDto {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Long categoriaId; // Recibimos el ID de la categoría
    private List<String> tallas;
    private List<String> colores;
    private boolean activo;
}