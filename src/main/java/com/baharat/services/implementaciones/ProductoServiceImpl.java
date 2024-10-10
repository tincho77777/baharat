package com.baharat.services.implementaciones;

import com.baharat.models.entities.Producto;
import com.baharat.repositories.ProductoRepository;
import com.baharat.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	protected ProductoRepository repository;

	@Override
	@Transactional(readOnly = true)
	//esto lo que hace es ofrecernos el begin(), el commit(), el rollbacK() al ejecutar una transaccion
	public List<Producto> consultarProductos() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Producto> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	@Transactional //estos no llevan el readOnly porque estos si ejecutan acciones que modifican la base de datos
	public Producto agregarProducto(Producto producto) {
		return repository.save(producto);
	}

	@Override
	@Transactional
	public void eliminarProducto(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Optional<Integer> consultarStockPorNombre(String nombreProducto) {
		Optional<Producto> productoOptional = repository.findByNombreProducto(nombreProducto);
		if (productoOptional.isPresent()) {
			return Optional.ofNullable(productoOptional.get().getStock());
		} else {
			throw new NoSuchElementException("Producto sin stock!");
		}
	}

	@Override
	public Optional<Producto> consultarProductoPorNombre(String nombreProducto) {
		Optional<Producto> productoOptional = repository.findByNombreProducto(nombreProducto);
		if (productoOptional.isPresent()) {
			return productoOptional;
		} else {
			throw new NoSuchElementException("Producto inexistente!");
		}
	}
}
