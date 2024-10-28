package com.baharat.exceptions;

import static com.baharat.parametros.Mensajes.ERROR_CREDENCIALES_INVALIDAS;

public class CredencialesInvalidasException extends Exception {

	private static final long serialVersionUID = 1L;

	public CredencialesInvalidasException(String message) {
		super(ERROR_CREDENCIALES_INVALIDAS + message);
	}
}
