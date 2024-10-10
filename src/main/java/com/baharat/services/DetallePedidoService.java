package com.baharat.services;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {

	DetallePedido agregarDetalle(DetallePedido detallePedido);

	void eliminarDetalle(Integer idDetalle);

	DetallePedido actualizarSubtotal(Integer idDetalle, Integer nuevaCantidad);

	List<DetallePedido> obtenerDetallesPorPedido(Pedido idPedido);

	Optional<DetallePedido> obtenerDetallesPorId(Integer id);

	void aplicarDescuento(BigDecimal porcentajeDescuento);

	void establecerNombreProducto(DetallePedido detalle);
	void establecerNombreProductoEnDetalles(List<DetallePedido> detalles);
}
