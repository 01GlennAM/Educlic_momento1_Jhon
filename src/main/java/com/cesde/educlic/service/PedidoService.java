package com.cesde.educlic.service;

import com.cesde.educlic.dto.PedidoDto;
import com.cesde.educlic.dto.DetallePedidoDto;
import com.cesde.educlic.model.entity.Pedido;
import com.cesde.educlic.model.entity.DetallePedido;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.repository.PedidoRepository;
import com.cesde.educlic.repository.ProductoRepository;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // 👈 Para buscar el usuario por ID

    @Autowired
    private ProductoRepository productoRepository; // 👈 Para buscar los productos de los detalles por ID

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido crearPedido(PedidoDto dto) {
        // 1. Buscar el usuario mediante el ID del DTO
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        // 2. Mapear los detalles del DTO a la entidad DetallePedido
        List<DetallePedido> detalles = null;
        if (dto.getDetalles() != null) {
            detalles = dto.getDetalles().stream().map(detDto -> {
                Producto producto = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detDto.getProductoId()));

                return DetallePedido.builder()
                        .producto(producto)
                        .cantidad(detDto.getCantidad())
                        .precioUnitario(detDto.getPrecioUnitario())
                        .build();
            }).collect(Collectors.toList());
        }

        // 3. Construir la entidad Pedido
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .detalles(detalles)
                .total(dto.getTotal())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoPedido.PENDIENTE)
                .build();

        // Aseguramos que los detalles apunten al pedido principal (relación bidireccional)
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
            }
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarPedido(Long id, PedidoDto dto) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            // 1. Buscar el usuario por ID
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

            // 2. Actualizar los campos principales del pedido
            pedidoExistente.setUsuario(usuario);
            pedidoExistente.setTotal(dto.getTotal());
            if (dto.getEstado() != null) {
                pedidoExistente.setEstado(dto.getEstado());
            }

            // 3. Manejo de los detalles del pedido (actualización de la relación)
            if (pedidoExistente.getDetalles() != null) {
                pedidoExistente.getDetalles().clear();
            }

            if (dto.getDetalles() != null) {
                List<DetallePedido> nuevosDetalles = dto.getDetalles().stream().map(detDto -> {
                    Producto producto = productoRepository.findById(detDto.getProductoId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detDto.getProductoId()));

                    return DetallePedido.builder()
                            .pedido(pedidoExistente)
                            .producto(producto)
                            .cantidad(detDto.getCantidad())
                            .precioUnitario(detDto.getPrecioUnitario())
                            .build();
                }).collect(Collectors.toList());

                pedidoExistente.getDetalles().addAll(nuevosDetalles);
            }

            return pedidoRepository.save(pedidoExistente);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado con el ID: " + id));
    }

    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}