package com.baharat.controllers;

import com.baharat.exceptions.RolInvalidoException;
import com.baharat.exceptions.UsuarioNoEncontradoException;
import com.baharat.models.entities.Usuario;
import com.baharat.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.baharat.parametros.Mensajes.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	protected UsuarioService usuarioService;

	@PostMapping("/registrar")
	public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
		try {
			usuarioService.crearUsuario(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(String.format(USUARIO_EMPLEADO_CREADO_CON_EXITO, usuario.getUsername()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_AL_REGISTRAR_USUARIO + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{idUsuario}/roles")
	public ResponseEntity<String> actualizarRol(@PathVariable Integer idUsuario,
	                                            @RequestParam String rol) {
		try {
			usuarioService.actualizarRol(idUsuario, rol);
			return ResponseEntity.ok("Rol actualizado correctamente.");
		} catch (UsuarioNoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (RolInvalidoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el rol: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<List<Usuario>> verUsuarios() {
		try {
			List<Usuario> usuarios = usuarioService.consultarUsuarios();
			if (usuarios.isEmpty()) {
				return ResponseEntity.noContent().build(); // Retornar 204 si no hay usuarios
			}

			return ResponseEntity.ok(usuarios);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/baja/{idUsuario}")
	public ResponseEntity<?> eliminarUsuarios(@PathVariable Integer idUsuario) {
		try {
			Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(idUsuario);
			if (usuarioOptional.isPresent()) {
				usuarioService.eliminarUsuario(idUsuario);
				return ResponseEntity.ok().body(null); // Retornar 204 si se elimina correctamente
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(ERROR_USUARIO_INEXISTENTE, idUsuario));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario: " + e.getMessage());
		}
	}

}
