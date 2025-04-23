package com.baharat.controllers.swagger;

import com.baharat.config.swagger.SwaggerConfig;
import com.baharat.models.entities.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = SwaggerConfig.PEDIDOS_TAG, description = SwaggerConfig.PEDIDO_TAG_DESCRIPTION)
public interface SwaggerPedidoController {

	@Operation(summary = SwaggerConfig.AGREGAR_PEDIDO_SUMMARY)
	@ApiResponse(
			responseCode = "201",
			description = "Pedido creado exitosamente")
	@PostMapping("/procesar")
	ResponseEntity<Pedido> procesarPedido(@RequestBody Pedido pedido);

	@Operation(summary = SwaggerConfig.OBTENER_PEDIDO_POR_ID_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Pedido encontrado")
	@GetMapping("/{id}")
	ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer id);

	@Operation(summary = SwaggerConfig.OBTENER_PEDIDOS_POR_ESTADO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Pedidos encontrados")
	@GetMapping("/estados")
	List<Pedido> obtenerPedidosPorEstado(@RequestParam(name = "estado") String estado);

	@Operation(summary = SwaggerConfig.OBTENER_FACTURA_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Factura encontrada")
	@GetMapping("/{idPedido}/factura")
	ResponseEntity<byte[]> generarFactura(@PathVariable Integer idPedido);
}
