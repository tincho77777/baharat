package com.baharat.services;

import com.baharat.models.entities.Pedido;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
	Pedido procesarPedido(Pedido pedido);

	List<Pedido> obtenerPedidosPorEstado(String estado);

	Pedido actualizarEstadoPedido(Integer idPedido, String nuevoEstado);

	Optional<Pedido> obtenerPedidoPorId(Integer idPedido);

	byte[] generarFacturaPDF(Optional<Pedido> pedido) throws IOException;
}
