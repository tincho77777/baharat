package com.baharat.repositories;

import com.baharat.models.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	Optional<Producto> findByNombreProducto(String nombreProducto);

	boolean existsByNombreProducto(String nombreProducto);
}
