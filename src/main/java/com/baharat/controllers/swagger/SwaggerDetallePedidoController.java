package com.baharat.controllers.swagger;

import com.baharat.config.swagger.SwaggerConfig;
import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = SwaggerConfig.DETALLE_PEDIDO_TAG, description = SwaggerConfig.DETALLE_PEDIDO_TAG_DESCRIPTION)
public interface SwaggerDetallePedidoController {

	@Operation(summary = SwaggerConfig.OBTENER_DETALLE_POR_ID_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)

	@GetMapping("/{id}")
	ResponseEntity<DetallePedido> getDetallePorId(@PathVariable Integer id);

	@Operation(summary = SwaggerConfig.OBTENER_DETALLE_POR_ID_PEDIDO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)
	@GetMapping("/pedidos/{idPedido}")
	ResponseEntity<List<DetallePedido>> getDetallePorIdPedido(@PathVariable Pedido idPedido);

}
