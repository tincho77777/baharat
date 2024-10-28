package com.baharat.exceptions;

import static com.baharat.parametros.Mensajes.ERROR_ROL_INVALIDO;

public class RolInvalidoException extends Exception {
	private static final long serialVersionUID = 1L;

	public RolInvalidoException(String message) {
		super(ERROR_ROL_INVALIDO + message);
	}
}
