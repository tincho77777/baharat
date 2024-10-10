package com.baharat.services;

import com.baharat.models.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
	List<Producto> consultarProductos();

	Optional<Producto> buscarPorId(Integer id);

	Producto agregarProducto(Producto producto);

	void eliminarProducto(Integer id);

	Optional<Integer> consultarStockPorNombre(String nombreProducto);

	Optional<Producto> consultarProductoPorNombre(String nombreProducto);
}
