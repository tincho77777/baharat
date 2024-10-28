package com.baharat.services.implementaciones;

import com.baharat.dtos.ProductoMasVendidoDTO;
import com.baharat.models.entities.Producto;
import com.baharat.repositories.ReporteRepository;
import com.baharat.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

	@Autowired
	protected ReporteRepository reporteRepository;

	@Override
	public List<ProductoMasVendidoDTO> obtenerProductosMasVendido(LocalDate fechaInicio, LocalDate fechaFin) {
		// Obtener la lista de productos más vendidos con la cantidad total vendida
		List<Object[]> productosVendidos = reporteRepository.findProductoMasVendidos(fechaInicio, fechaFin);

		// Convertir cada resultado a ProductoMasVendidoDTO y devolver la lista
		return productosVendidos.stream()
				.map(resultado -> new ProductoMasVendidoDTO((Producto) resultado[0], (Long) resultado[1]))
				.collect(Collectors.toList());
	}

	@Override
	public List<Producto> obtenerProductosConBajoStock(Integer limiteStock) {
		return reporteRepository.findProductosConBajoStock(limiteStock);
	}

	@Override
	public BigDecimal obtenerVentasTotales(LocalDate fechaInicio, LocalDate fechaFin) {
		return reporteRepository.calcularVentasTotales(fechaInicio, fechaFin);
	}
}
