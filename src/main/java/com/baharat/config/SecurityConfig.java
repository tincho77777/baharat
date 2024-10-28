package com.baharat.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Data
@NoArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {

		http.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/admin/**").hasRole("ADMIN") // Solo administradores
						.requestMatchers("/empleado/**").hasRole("EMPLEADO") // Solo empleados
						.requestMatchers("/usuarios/registrar", "/auth/login").permitAll() // Permitir acceso público a login y registro
						.anyRequest().authenticated() // Todas las demás rutas requieren autenticación
				)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)  // Agregar el filtro JWT antes del filtro de autenticación de Spring Security
				.logout(LogoutConfigurer::permitAll)
				.csrf(AbstractHttpConfigurer::disable); // Deshabilitar CSRF si es necesario

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}

