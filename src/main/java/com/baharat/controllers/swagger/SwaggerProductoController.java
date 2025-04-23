package com.baharat.controllers.swagger;

import com.baharat.config.swagger.SwaggerConfig;
import com.baharat.models.entities.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = SwaggerConfig.PRODUCTOS_TAG, description = SwaggerConfig.PRODUCTO_TAG_DESCRIPTION)
public interface SwaggerProductoController {

	@Operation(summary = SwaggerConfig.LISTAR_PRODUCTOS_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = SwaggerConfig.RESPUESTA_200)
	@GetMapping
	ResponseEntity<List<Producto>> listarProductos();

	@Operation(summary = SwaggerConfig.OBTENER_PRODUCTO_POR_ID_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Producto encontrado")
	@ApiResponse(
			responseCode = "404",
			description = "Producto no encontrado")
	@GetMapping("/{id}")
	ResponseEntity<?> getProductoPorId(@PathVariable Integer id);

	@Operation(summary = SwaggerConfig.CONSULTAR_STOCK_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Stock obtenido exitosamente")
	@GetMapping("/stock")
	ResponseEntity<Integer> getStockPorNombre(@RequestParam String nombreProducto);

	@Operation(summary = SwaggerConfig.OBTENER_PRODUCTO_POR_NOMBRE_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Producto encontrado")
	@ApiResponse(
			responseCode = "404",
			description = "Producto no encontrado")
	@GetMapping("/producto")
	ResponseEntity<Producto> getProductoPorNombre(@RequestParam String nombreProducto);

	@Operation(summary = SwaggerConfig.AGREGAR_PRODUCTO_SUMMARY)
	@ApiResponse(
			responseCode = "201",
			description = "Producto creado exitosamente")
	@PostMapping("/alta")
	ResponseEntity<?> agregarProductoABase(@RequestBody Producto producto);

	@Operation(summary = SwaggerConfig.ELIMINAR_PRODUCTO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Producto eliminado exitosamente")
	@ApiResponse(
			responseCode = "404",
			description = "Producto no encontrado")
	@DeleteMapping("/baja/{id}")
	ResponseEntity<String> eliminarProductoBase(@PathVariable Integer id);

	@Operation(summary = SwaggerConfig.EDITAR_PRODUCTO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Producto actualizado exitosamente")
	@PutMapping("/editar/{id}")
	ResponseEntity<?> editarProducto(@PathVariable Integer id, @RequestBody Producto producto);
}

