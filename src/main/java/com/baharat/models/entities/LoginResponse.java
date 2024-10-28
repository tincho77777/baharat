package com.baharat.models.entities;

import lombok.Data;

@Data
public class LoginResponse {
	// Clase para la respuesta de login
	private final String token;
	private final String message;

	public LoginResponse(String token, String message) {
		this.token = token;
		this.message = message;
	}
}

