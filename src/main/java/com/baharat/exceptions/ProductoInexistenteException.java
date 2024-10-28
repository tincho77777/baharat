package com.baharat.exceptions;

public class ProductoInexistenteException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProductoInexistenteException(String message) {
		super(message);
	}

}
