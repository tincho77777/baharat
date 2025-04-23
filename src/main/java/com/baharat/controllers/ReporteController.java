package com.baharat.controllers;

import com.baharat.controllers.swagger.SwaggerReporteController;
import com.baharat.dtos.ProductoMasVendidoDTO;
import com.baharat.models.entities.Producto;
import com.baharat.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController implements SwaggerReporteController {

	@Autowired
	protected ReporteService reporteService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/productos-preferidos")
	public ResponseEntity<List<ProductoMasVendidoDTO>> obtenerProductoMasVendido(@RequestParam(name = "fecha_inicio") LocalDate fechaInicio,
	                                                                             @RequestParam(name = "fecha_fin") LocalDate fechaFin) {
		List<ProductoMasVendidoDTO> productos = reporteService.obtenerProductosMasVendido(fechaInicio, fechaFin);
		if (productos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(productos);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/productos-bajo-stock")
	public ResponseEntity<List<Producto>> obtenerProductosConBajoStock(@RequestParam(name = "limite_stock") Integer limiteStock) {
		List<Producto> productos = reporteService.obtenerProductosConBajoStock(limiteStock);
		if (productos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(productos);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/ventas-totales")
	public ResponseEntity<BigDecimal> obtenerVentasTotales(@RequestParam(name = "fecha_inicio") LocalDate fechaInicio,
	                                                       @RequestParam(name = "fecha_fin") LocalDate fechaFin) {
		BigDecimal ventasTotales = reporteService.obtenerVentasTotales(fechaInicio, fechaFin);
		if (ventasTotales == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(ventasTotales);
	}
}
