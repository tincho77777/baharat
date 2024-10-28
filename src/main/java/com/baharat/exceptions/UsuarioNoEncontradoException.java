package com.baharat.exceptions;

import static com.baharat.parametros.Mensajes.USUARIO_NO_ENCONTRADO;

public class UsuarioNoEncontradoException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontradoException(String message) {
		super(USUARIO_NO_ENCONTRADO + message);
	}
}
