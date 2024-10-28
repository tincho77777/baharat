package com.baharat.exceptions;

public class ProductoYaExisteException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProductoYaExisteException(String message) {
		super(message);
	}
}
