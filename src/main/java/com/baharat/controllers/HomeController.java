package com.baharat.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/home")
public class HomeController {

	@GetMapping
	public ResponseEntity<String> homePage(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String rol = userDetails.getAuthorities().stream()
				.findFirst()
				.map(GrantedAuthority::getAuthority)
				.orElse("ROLE_EMPLEADO"); // Asumir EMPLEADO como valor por defecto si no hay rol

		if ("ROLE_ADMIN".equals(rol)) {
			return ResponseEntity.ok("Bienvenido Admin. Aquí puedes gestionar inventarios, usuarios, y ver reportes.");
		} else if ("ROLE_EMPLEADO".equals(rol)) {
			return ResponseEntity.ok("Bienvenido Empleado. Aquí puedes gestionar pedidos, consultar stock y productos.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado.");
		}
	}
}

