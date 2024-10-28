package com.baharat.services;

import com.baharat.dtos.ProductoMasVendidoDTO;
import com.baharat.models.entities.Producto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
	List<ProductoMasVendidoDTO> obtenerProductosMasVendido(LocalDate fechaInicio, LocalDate fechaFin);

	List<Producto> obtenerProductosConBajoStock(Integer limiteStock);

	BigDecimal obtenerVentasTotales(LocalDate fechaInicio, LocalDate fechaFin);
}
