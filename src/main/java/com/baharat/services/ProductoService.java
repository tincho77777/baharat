package com.baharat.services;

import com.baharat.exceptions.ProductoInexistenteException;
import com.baharat.exceptions.ProductoYaExisteException;
import com.baharat.exceptions.StockInsuficienteException;
import com.baharat.models.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
	List<Producto> consultarProductos();

	Optional<Producto> buscarPorId(Integer id) throws ProductoInexistenteException;

	Producto agregarProducto(Producto producto) throws ProductoYaExisteException;

	void eliminarProducto(Integer id) throws ProductoInexistenteException;

	Optional<Integer> consultarStockPorNombre(String nombreProducto) throws StockInsuficienteException;

	Optional<Producto> consultarProductoPorNombre(String nombreProducto) throws ProductoInexistenteException;
}
