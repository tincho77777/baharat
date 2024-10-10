package com.baharat.controllers;

import com.baharat.models.entities.Producto;
import com.baharat.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductoController {
	@Autowired
	protected ProductoService productoService;

	@GetMapping("/productos/")
	public List<Producto> listarProductos() {
		return productoService.consultarProductos();
	}

	@GetMapping("/productos/{id}")
	public ResponseEntity<Producto> getProductoPorId(@PathVariable Integer id) { //usamos el @PathVariable para que el valor que estamos pasando ahi en este caso el id, directamente lo reemplaze en el id parametrizado del GetMapping, esto siempre y cuando los dos valores se llamen igual
		Optional<Producto> productoOptional = productoService.buscarPorId(id);
		return productoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/productos/stock")
	public ResponseEntity<Integer> getStockPorNombre(@RequestParam(name = "nombre_producto") String nombreProducto) {
		Optional<Integer> stockOptional = productoService.consultarStockPorNombre(nombreProducto);
		return stockOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
	}

	@GetMapping("/productos/producto")
	public ResponseEntity<Producto> getProductoPorNombre(@RequestParam(name = "nombre_producto") String nombreProducto) {
		Optional<Producto> productoOptional = productoService.consultarProductoPorNombre(nombreProducto);
		return productoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
	}

	@PostMapping("/productos/alta")
	public ResponseEntity<Producto> agregarProductoABase(@RequestBody Producto producto) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregarProducto(producto));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@DeleteMapping("/productos/baja/{id}")
	public ResponseEntity<Producto> eliminarProductoBase(@PathVariable Integer id) {
		Optional<Producto> productoOptional = productoService.buscarPorId(id);
		if (productoOptional.isPresent()) {
			productoService.eliminarProducto(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/productos/editar/{id}")
	public ResponseEntity<Producto> editarProducto(@PathVariable Integer id,
	                                               @RequestBody Producto producto) {

		Optional<Producto> productoOptional = productoService.buscarPorId(id);
		if (productoOptional.isPresent()) {
			Producto productoDb = productoOptional.get();

			productoDb.setNombreProducto(producto.getNombreProducto());
			productoDb.setCategoria(producto.getCategoria());
			productoDb.setStock(producto.getStock());
			productoDb.setFechaIngreso(producto.getFechaIngreso());
			productoDb.setPrecio(producto.getPrecio());
			productoDb.setIsGlutenFree(producto.getIsGlutenFree());
			productoDb.setIsVegano(producto.getIsVegano());
			return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregarProducto(productoDb));
		}
		return ResponseEntity.notFound().build();
	}
}
