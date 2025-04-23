package com.baharat.services.implementaciones;

import com.baharat.exceptions.ProductoInexistenteException;
import com.baharat.exceptions.ProductoYaExisteException;
import com.baharat.exceptions.StockInsuficienteException;
import com.baharat.models.entities.Producto;
import com.baharat.repositories.ProductoRepository;
import com.baharat.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.baharat.parametros.Mensajes.ERROR_ELIMINAR_PRODUCTO;
import static com.baharat.parametros.Mensajes.PRODUCTO_EXISTENTE;

@Service
@Validated
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	protected ProductoRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> consultarProductos() {
		List<Producto> productos = repository.findAll();
		if (productos.isEmpty()) {
			return Collections.emptyList();
		}
		return productos;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Producto> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Producto agregarProducto(@Valid Producto producto) throws ProductoYaExisteException {
		if (repository.existsByNombreProducto(producto.getNombreProducto())) {
			throw new ProductoYaExisteException(String.format(PRODUCTO_EXISTENTE, producto.getNombreProducto()));
		}
		return repository.save(producto);
	}

	@Override
	@Transactional
	public void eliminarProducto(Integer id) throws ProductoInexistenteException {
		if (!repository.existsById(id)) {
			throw new ProductoInexistenteException(String.format(ERROR_ELIMINAR_PRODUCTO, id));
		}
		repository.deleteById(id);
	}

	@Override
	public Optional<Integer> consultarStockPorNombre(String nombreProducto) throws StockInsuficienteException {
		Optional<Producto> productoOptional = repository.findByNombreProducto(nombreProducto);
		if (productoOptional.isPresent()) {
			return Optional.ofNullable(productoOptional.get().getStock());
		} else {
			throw new StockInsuficienteException();
		}
	}

	@Override
	public Optional<Producto> consultarProductoPorNombre(String nombreProducto) throws ProductoInexistenteException {
		Optional<Producto> productoOptional = repository.findByNombreProducto(nombreProducto);
		if (productoOptional.isPresent()) {
			return productoOptional;
		} else {
			throw new ProductoInexistenteException("Producto inexistente!");
		}
	}
}
