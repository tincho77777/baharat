package com.baharat.services.implementaciones;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import com.baharat.models.entities.Producto;
import com.baharat.repositories.DetallePedidoRepository;
import com.baharat.repositories.ProductoRepository;
import com.baharat.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoImpl implements DetallePedidoService {

	@Autowired
	protected ProductoRepository productoRepository;
	@Autowired
	protected DetallePedidoRepository detalleRepository;

	@Override
	public DetallePedido agregarDetalle(DetallePedido detallePedido) {
		// Verificar si el producto existe
		Producto producto = productoRepository.findById(detallePedido.getProducto().getIdProducto())
				.orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detallePedido.getProducto().getIdProducto()));

		// Calcular el subtotal basado en el precio del producto y la cantidad
		detallePedido.setSubTotal(producto.getPrecio().multiply(BigDecimal.valueOf(detallePedido.getCantidad())));

		// Guardar el detalle en la base de datos
		return detalleRepository.save(detallePedido);
	}

	@Override
	public void eliminarDetalle(Integer idDetalle) {
		// Verificar si el detalle existe
		DetallePedido detalle = detalleRepository.findById(idDetalle)
				.orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado: " + idDetalle));

		detalleRepository.delete(detalle);
	}

	@Override
	public DetallePedido actualizarSubtotal(Integer idDetalle, Integer nuevaCantidad) {
		// Buscar el detalle del pedido
		DetallePedido detalle = detalleRepository.findById(idDetalle)
				.orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado: " + idDetalle));

		// Verificar si el producto asociado existe
		Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
				.orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getIdProducto()));

		// Actualizar la cantidad
		detalle.setCantidad(nuevaCantidad);

		// Recalcular el subtotal
		detalle.setSubTotal(producto.getPrecio().multiply(BigDecimal.valueOf(nuevaCantidad)));

		// Guardar el detalle actualizado
		return detalleRepository.save(detalle);
	}

	@Override
	public List<DetallePedido> obtenerDetallesPorPedido(Pedido idPedido) {
		return detalleRepository.findByPedido(idPedido);
	}

	@Override
	public Optional<DetallePedido> obtenerDetallesPorId(Integer id) {
		return detalleRepository.findById(id);
	}

	@Override
	public void aplicarDescuento(BigDecimal porcentajeDescuento) {
	}

	public void establecerNombreProducto(DetallePedido detalle) {
		if (detalle != null && detalle.getProducto() != null) {
			detalle.setNombreProducto(detalle.getProducto().getNombreProducto());
		}
	}

	public void establecerNombreProductoEnDetalles(List<DetallePedido> detalles) {
		for (DetallePedido detalle : detalles) {
			establecerNombreProducto(detalle);
		}
	}
}
