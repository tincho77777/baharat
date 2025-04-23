package com.baharat.controllers.swagger;

import com.baharat.config.swagger.SwaggerConfig;
import com.baharat.models.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = SwaggerConfig.USUARIOS_TAG, description = SwaggerConfig.USUARIO_TAG_DESCRIPTION)
public interface SwaggerUsuarioController {

	@Operation(summary = SwaggerConfig.AGREGAR_USUARIO_SUMMARY)
	@ApiResponse(
			responseCode = "201",
			description = "Usuario creado exitosamente")
	@PostMapping("/registrar")
	ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario);

	@Operation(summary = SwaggerConfig.EDITAR_ROL_USUARIO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Usuario modificado exitosamente")
	@PutMapping("/{idUsuario}/roles")
	ResponseEntity<String> actualizarRol(@PathVariable Integer idUsuario, @RequestParam String rol);

	@Operation(summary = SwaggerConfig.LISTAR_USUARIOS_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Usuarios encontrados")
	@GetMapping()
	ResponseEntity<List<Usuario>> verUsuarios();

	@Operation(summary = SwaggerConfig.ELIMINAR_USUARIO_SUMMARY)
	@ApiResponse(
			responseCode = "200",
			description = "Usuario eliminado exitosamente")
	@DeleteMapping("/baja/{idUsuario}")
	ResponseEntity<?> eliminarUsuarios(@PathVariable Integer idUsuario);
}
