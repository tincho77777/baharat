package com.baharat.controllers;

import com.baharat.controllers.swagger.SwaggerProductoController;
import com.baharat.exceptions.ProductoInexistenteException;
import com.baharat.exceptions.ProductoYaExisteException;
import com.baharat.exceptions.StockInsuficienteException;
import com.baharat.models.entities.Producto;
import com.baharat.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.baharat.parametros.Mensajes.ERROR_PRODUCTO_NO_ENCONTRADO;
import static com.baharat.parametros.Mensajes.PRODUCTO_EXISTENTE;

@RestController
@RequestMapping("/productos")
@Validated
public class ProductoController implements SwaggerProductoController {
	@Autowired
	protected ProductoService productoService;

	@GetMapping()
	public ResponseEntity<List<Producto>> listarProductos() {
		try {
			List<Producto> productos = productoService.consultarProductos();
			if (productos.isEmpty()) {
				return ResponseEntity.noContent().build(); // Retornar 204 si no hay productos
			}
			return ResponseEntity.status(HttpStatus.OK).body(productos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductoPorId(@PathVariable Integer id) { //usamos el @PathVariable para que el valor que estamos pasando ahi en este caso el id, directamente lo reemplaze en el id parametrizado del GetMapping, esto siempre y cuando los dos valores se llamen igual
		try {
			Optional<Producto> productoOptional = productoService.buscarPorId(id);
			if (productoOptional.isPresent()) {
				return ResponseEntity.ok(productoOptional);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al buscar el producto: " + e.getMessage());
		}
	}

	@GetMapping("/stock")
	public ResponseEntity<Integer> getStockPorNombre(@RequestParam(name = "nombre_producto") String nombreProducto) {
		try {
			Integer stock = productoService.consultarStockPorNombre(nombreProducto)
					.orElseThrow(() -> new StockInsuficienteException(nombreProducto));
			return ResponseEntity.ok(stock);
		} catch (StockInsuficienteException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}

	@GetMapping("/producto")
	public ResponseEntity<Producto> getProductoPorNombre(@RequestParam(name = "nombre_producto") String nombreProducto) {
		try {
			Producto producto = productoService.consultarProductoPorNombre(nombreProducto)
					.orElseThrow(() -> new ProductoInexistenteException("Producto inexistente: " + nombreProducto));
			return ResponseEntity.ok(producto);
		} catch (ProductoInexistenteException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/alta")
	public ResponseEntity<?> agregarProductoABase(@RequestBody @Valid Producto producto) {
		try {
			Producto nuevoProducto = productoService.agregarProducto(producto);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
		} catch (ProductoYaExisteException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(PRODUCTO_EXISTENTE, producto.getNombreProducto()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al agregar el producto: " + e.getMessage());
		}
	}

	@DeleteMapping("/baja/{id}")
	public ResponseEntity<String> eliminarProductoBase(@PathVariable Integer id) {
		try {
			Optional<Producto> productoOptional = productoService.buscarPorId(id);
			if (productoOptional.isPresent()) {
				productoService.eliminarProducto(id);
				return ResponseEntity.ok().body(null); // Retornar 204 si se elimina correctamente
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(ERROR_PRODUCTO_NO_ENCONTRADO, id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto: " + e.getMessage());
		}
	}

	@PutMapping("/editar/{id}")
	public ResponseEntity<?> editarProducto(@PathVariable Integer id,
	                                        @RequestBody @Valid Producto producto) {
		try {
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
				Producto productoActualizado = productoService.agregarProducto(productoDb);
				return ResponseEntity.ok(productoActualizado); // Retornar 200 con el producto actualizado
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(ERROR_PRODUCTO_NO_ENCONTRADO, id));
		} catch (ProductoYaExisteException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(PRODUCTO_EXISTENTE, producto.getNombreProducto()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al editar el producto: " + e.getMessage());
		}
	}
}
