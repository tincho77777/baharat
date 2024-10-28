package com.baharat.controllers;

import com.baharat.exceptions.CredencialesInvalidasException;
import com.baharat.exceptions.UsuarioNoEncontradoException;
import com.baharat.models.entities.LoginRequest;
import com.baharat.models.entities.LoginResponse;
import com.baharat.models.entities.Usuario;
import com.baharat.security.JwtUtil;
import com.baharat.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baharat.parametros.Mensajes.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	protected UsuarioService usuarioService;

	@Autowired
	protected JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			Usuario usuario = usuarioService.obtenerPorUsername(loginRequest.getUsername());
			if (usuario == null) {
				throw new UsuarioNoEncontradoException(String.format(USERNAME_USUARIO_NO_ENCONTRADO, loginRequest.getUsername()));
			}

			// Verificar la contraseña
			if (!usuarioService.verificarPassword(loginRequest.getPassword(), usuario.getPassword())) {
				throw new CredencialesInvalidasException(String.format(CONSTRASENA_INCORRECTA, loginRequest.getUsername()));
			}

			// Generar el token JWT
			String token = jwtUtil.generateToken(usuario.getUsername());

			return ResponseEntity.ok(new LoginResponse(token, LOGIN_EXITOSO));
		} catch (UsuarioNoEncontradoException | CredencialesInvalidasException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error en el login: " + e.getMessage());
		}
	}
}
