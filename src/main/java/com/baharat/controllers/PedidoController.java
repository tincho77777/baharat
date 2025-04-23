package com.baharat.controllers;

import com.baharat.controllers.swagger.SwaggerPedidoController;
import com.baharat.models.entities.Pedido;
import com.baharat.services.PedidoService;
import com.baharat.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements SwaggerPedidoController {

	@Autowired
	protected PedidoService pedidoService;

	@PostMapping("/procesar")
	public ResponseEntity<Pedido> procesarPedido(@RequestBody Pedido pedido) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.procesarPedido(pedido));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer id) {
		Optional<Pedido> pedidoOptional = pedidoService.obtenerPedidoPorId(id);
		return pedidoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/estados")
	public List<Pedido> obtenerPedidosPorEstado(@RequestParam(name = "estado") String estado) {
		return pedidoService.obtenerPedidosPorEstado(estado);
	}

	@GetMapping("/{idPedido}/factura")
	public ResponseEntity<byte[]> generarFactura(@PathVariable Integer idPedido) {
		try {
			Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(idPedido);
			byte[] pdf = pedidoService.generarFacturaPDF(pedido);

			// Devolver el archivo PDF como respuesta HTTP
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + idPedido + ".pdf")
					.contentType(MediaType.APPLICATION_PDF)
					.body(pdf);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
