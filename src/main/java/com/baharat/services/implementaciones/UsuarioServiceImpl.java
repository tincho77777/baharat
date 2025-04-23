package com.baharat.services.implementaciones;

import com.baharat.exceptions.DatabaseException;
import com.baharat.exceptions.RolInvalidoException;
import com.baharat.exceptions.UsuarioNoEncontradoException;
import com.baharat.exceptions.UsuarioYaExisteException;
import com.baharat.models.entities.Usuario;
import com.baharat.repositories.UsuarioRepository;
import com.baharat.services.UsuarioService;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baharat.parametros.Mensajes.*;
import static com.baharat.parametros.Parametros.ADMIN;
import static com.baharat.parametros.Parametros.EMPLEADO;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

	@Autowired
	protected UsuarioRepository usuarioRepository;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected Validator validator;

	@Override
	@Transactional
	public Usuario crearUsuario(Usuario usuario) throws UsuarioYaExisteException, DatabaseException {
		// Validar que el usuario no exista ya en la base de datos
		if (usuarioRepository.existsByUsername(usuario.getUsername())) {
			throw new UsuarioYaExisteException(String.format(ERROR_NOMBRE_USUARIO_EXISTENTE, usuario.getUsername()));
		}

		// Valida el objeto Usuario
		Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
		if (!violations.isEmpty()) {
			StringBuilder errors = new StringBuilder();
			for (ConstraintViolation<Usuario> violation : violations) {
				errors.append(violation.getMessage()).append("; "); //encadena los mensajes que colocamos en la entity
			}
			throw new ConstraintViolationException(errors.toString(), violations);
		}

		// Encriptar la contraseña antes de guardarla
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		// Asignamos al usuario por defecto como empleado
		usuario.setRol(EMPLEADO);
		usuario.setFechaAlta(LocalDate.now());

		try {
			return usuarioRepository.save(usuario);
		} catch (Exception e) {
			throw new DatabaseException(ERROR_AL_GUARDAR_USUARIO + e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> consultarUsuarios() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		if (usuarios.isEmpty()) {
			return Collections.emptyList();
		}
		return usuarios;
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario obtenerPorEmail(String email) throws UsuarioNoEncontradoException {
		// Validar el formato del email antes de la búsqueda
		if (!isValidEmail(email)) {
			throw new IllegalArgumentException(ERROR_EMAIL_INVALIDO);
		}

		// Buscar el usuario por email en la base de datos
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsuarioNoEncontradoException(ERROR_USER_EMAIL_NO_ENCONTRADO + email));
	}

	private boolean isValidEmail(String email) {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
			return true;
		} catch (AddressException e) {
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario obtenerPorUsername(String username) throws UsuarioNoEncontradoException {
		Usuario usuario = usuarioRepository.findByUsername(username);
		if (usuario == null) {
			throw new UsuarioNoEncontradoException(String.format(ERROR_USUARIO_INEXISTENTE, username));
		}
		return usuario;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(USUARIO_NO_ENCONTRADO);
		}

		// Convertir el rol del usuario (String) a GrantedAuthority
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

		return new User(usuario.getUsername(), usuario.getPassword(), authorities);
	}

	@Override
	@Transactional
	public void actualizarRol(Integer usuarioId,
	                          String nuevoRol) throws UsuarioNoEncontradoException, RolInvalidoException {
		Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNoEncontradoException(
				"Usuario con ID: " + usuarioId + " no encontrado")
		);

		// verifica si el rol es válido
		if (!ADMIN.equals(nuevoRol) && !EMPLEADO.equals(nuevoRol)) {
			throw new RolInvalidoException(String.format(ERROR_NUEVO_ROL_INVALIDO, nuevoRol));
		}

		// verifica que el nuevo rol no sea el mismo que ya tiene
		if (usuario.getRol().equals(nuevoRol)) {
			throw new RolInvalidoException(String.format(ERROR_ROL_YA_ASIGNADO, usuario, nuevoRol));
		}

		usuario.setRol(nuevoRol);
		usuarioRepository.save(usuario);
	}

	@Override
	public boolean verificarPassword(String rawPassword,
	                                 String encodedPassword) {
		// Aquí deberías usar un método de tu elección para comparar contraseñas (como BCrypt)
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	@Override
	@Transactional
	public void eliminarUsuario(Integer idUsuario) throws UsuarioNoEncontradoException {
		if (!usuarioRepository.existsById(idUsuario)) {
			throw new UsuarioNoEncontradoException(String.valueOf(idUsuario));
		}
		usuarioRepository.deleteById(idUsuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorId(Integer id) {
		return usuarioRepository.findById(id);
	}

}
