package com.baharat.exceptions;

import static com.baharat.parametros.Mensajes.ERROR_USUARIO_EXISTENTE;

public class UsuarioYaExisteException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioYaExisteException(String message) {
		super(ERROR_USUARIO_EXISTENTE + message);
	}

}
