package com.baharat.repositories.repositorioPersonalizado;

import java.time.LocalDate;
import java.util.List;

public interface ReporteRepositoryCustom {

	List<Object[]> findProductoMasVendidos(LocalDate fechaInicio, LocalDate fechaFin);

}
