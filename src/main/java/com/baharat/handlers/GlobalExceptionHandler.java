package com.baharat.handlers;

import com.baharat.exceptions.CredencialesInvalidasException;
import com.baharat.exceptions.UsuarioNoEncontradoException;
import com.baharat.exceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Maneja la excepción específica de UsuarioNoEncontradoException
	@ExceptionHandler(UsuarioNoEncontradoException.class)
	public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	public ResponseEntity<String> handleCredencialesInvalidasException(CredencialesInvalidasException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(UsuarioYaExisteException.class)
	public ResponseEntity<String> handleUsuarioYaExisteException(UsuarioYaExisteException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + e.getMessage());
	}

	// Maneja excepciones de validación
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errors = new StringBuilder("Errores de validación: ");

		ex.getBindingResult().getFieldErrors().forEach(error -> errors.append(String.format(
				"%s: %s. ", error.getField(), error.getDefaultMessage()))
		);

		return ResponseEntity.badRequest().body(errors.toString());
	}

	// Maneja cualquier otra excepción general
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
		return new ResponseEntity<>("Ha ocurrido un error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
