package com.baharat.controllers.swagger;

import com.baharat.config.swagger.SwaggerConfig;
import com.baharat.dtos.ProductoMasVendidoDTO;
import com.baharat.models.entities.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = SwaggerConfig.REPORTES_TAG, description = SwaggerConfig.PRODUCTO_TAG_DESCRIPTION)
public interface SwaggerReporteController {


	@Operation(summary = SwaggerConfig.LISTAR_PRODUCTOS_PREFERIDOS_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)
	@GetMapping("/productos-preferidos")
	ResponseEntity<List<ProductoMasVendidoDTO>> obtenerProductoMasVendido(@RequestParam(name = "fecha_inicio") LocalDate fechaInicio,
	                                                                      @RequestParam(name = "fecha_fin") LocalDate fechaFin);


	@Operation(summary = SwaggerConfig.LISTAR_PRODUCTOS_CON_BAJO_STOCK_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)
	@GetMapping("/productos-bajo-stock")
	ResponseEntity<List<Producto>> obtenerProductosConBajoStock(@RequestParam(name = "limite_stock") Integer limiteStock);


	@Operation(summary = SwaggerConfig.LISTAR_VENTAS_TOTALES_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)
	@GetMapping("/ventas-totales")
	ResponseEntity<BigDecimal> obtenerVentasTotales(@RequestParam(name = "fecha_inicio") LocalDate fechaInicio,
	                                                @RequestParam(name = "fecha_fin") LocalDate fechaFin);
}
