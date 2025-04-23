package com.baharat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// Permitir acceso a los endpoints de la API
		registry.addMapping("/api/**") // Configura los endpoints permitidos
				.allowedOrigins("http://localhost:4200") // Permite el acceso desde el frontend en Angular
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

		// Permitir acceso a los endpoints de Swagger
		registry.addMapping("/swagger-ui.html") // Página principal de Swagger
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

		registry.addMapping("/swagger-ui/**") // Recursos de Swagger UI
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

		registry.addMapping("/v3/api-docs/**") // Documentación de la API
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

		registry.addMapping("/swagger-resources/**") // Recursos de Swagger
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

		registry.addMapping("/webjars/**") // Archivos estáticos utilizados por Swagger UI
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);

	}
}
