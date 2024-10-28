package com.baharat.repositories;

import com.baharat.models.entities.Producto;
import com.baharat.models.entities.Reporte;
import com.baharat.repositories.repositorioPersonalizado.ReporteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer>, ReporteRepositoryCustom {

	@Query("SELECT p FROM Producto p WHERE p.stock < :limiteStock ORDER BY p.stock ASC")
	List<Producto> findProductosConBajoStock(@Param("limiteStock") Integer limiteStock);

	@Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
	BigDecimal calcularVentasTotales(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

}
