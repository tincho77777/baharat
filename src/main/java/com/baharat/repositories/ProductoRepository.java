package com.baharat.repositories;

import com.baharat.models.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	Optional<Producto> findByNombreProducto(String nombreProducto);
}
