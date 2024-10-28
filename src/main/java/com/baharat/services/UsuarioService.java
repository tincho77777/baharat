package com.baharat.services;

import com.baharat.exceptions.DatabaseException;
import com.baharat.exceptions.RolInvalidoException;
import com.baharat.exceptions.UsuarioNoEncontradoException;
import com.baharat.exceptions.UsuarioYaExisteException;
import com.baharat.models.entities.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
	Usuario crearUsuario(Usuario usuario) throws UsuarioYaExisteException, DatabaseException;

	List<Usuario> consultarUsuarios();

	Usuario obtenerPorEmail(String email) throws UsuarioNoEncontradoException;

	Usuario obtenerPorUsername(String username) throws UsuarioNoEncontradoException;

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	void actualizarRol(Integer usuarioId, String nuevoRol) throws UsuarioNoEncontradoException, RolInvalidoException;

	boolean verificarPassword(String rawPassword, String encodedPassword);

	void eliminarUsuario(Integer idUsuario) throws UsuarioNoEncontradoException;

	Optional<Usuario> buscarPorId(Integer id);

}
