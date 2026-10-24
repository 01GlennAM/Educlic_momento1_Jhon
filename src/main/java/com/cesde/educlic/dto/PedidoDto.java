package com.cesde.educlic.dto;

import java.math.BigDecimal;
import java.util.List;
import com.cesde.educlic.model.enums.EstadoPedido;
import lombok.Data;

@Data
public class PedidoDto {
    private Long usuarioId; // Recibimos el ID del usuario
    private List<DetallePedidoDto> detalles;
    private BigDecimal total;
    private EstadoPedido estado;
}