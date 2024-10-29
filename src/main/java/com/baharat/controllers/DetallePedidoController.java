package com.baharat.controllers;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import com.baharat.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/detalle-pedidos")
public class DetallePedidoController {
	@Autowired
	protected DetallePedidoService detallePedidoService;

	@GetMapping("/{id}")
	public ResponseEntity<DetallePedido> getDetallePorId(@PathVariable Integer id) {
		Optional<DetallePedido> detalleOptional = detallePedidoService.obtenerDetallesPorId(id);

		detalleOptional.ifPresent(detalle -> detallePedidoService.establecerNombreProducto(detalle));
		return detalleOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/pedidos/{idPedido}")
	public ResponseEntity<List<DetallePedido>> getDetallePorIdPedido(@PathVariable Pedido idPedido) {
		List<DetallePedido> detalles = detallePedidoService.obtenerDetallesPorPedido(idPedido);

		if (detalles.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			detallePedidoService.establecerNombreProductoEnDetalles(detalles);
			return ResponseEntity.ok(detalles);
		}
	}

}
