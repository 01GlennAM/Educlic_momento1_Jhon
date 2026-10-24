package com.cesde.educlic.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetallePedidoDto {
    private Long productoId; // Recibimos el ID del producto
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
