package com.baharat.exceptions;

public class StockInsuficienteException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockInsuficienteException() {
		super("Producto sin stock: ");
	}

	public StockInsuficienteException(String message) {
		super("Producto sin stock: " + message);
	}

}
