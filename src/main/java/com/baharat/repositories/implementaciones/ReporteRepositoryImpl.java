package com.baharat.repositories.implementaciones;

import com.baharat.repositories.repositorioPersonalizado.ReporteRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

public class ReporteRepositoryImpl implements ReporteRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	public List<Object[]> findProductoMasVendidos(LocalDate fechaInicio, LocalDate fechaFin) {
		var jpql = "SELECT p, SUM(dp.cantidad) AS totalVendidos "
				+ "FROM DetallePedido dp "
				+ "JOIN dp.producto p "
				+ "JOIN dp.pedido pe "
				+ "WHERE pe.fechaPedido BETWEEN :fechaInicio AND :fechaFin "
				+ "GROUP BY p "
				+ "ORDER BY totalVendidos DESC";

		var query = entityManager.createQuery(jpql);
		query.setParameter("fechaInicio", fechaInicio);
		query.setParameter("fechaFin", fechaFin);
		query.setMaxResults(5); // Limita el resultado a los 5 productos más vendidos

		return query.getResultList(); // Retorna una lista de objetos [Producto, Long]
	}
}
