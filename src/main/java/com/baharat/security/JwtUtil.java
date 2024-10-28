package com.baharat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

	// Genera una clave segura usando la clase Keys
	private static final String SECRET_KEY = "a1B2c3D4e5F6g7H8i9J0kLmNoPqRsTuVwXyZ1234567890abcdefghijklmnopqrstuvwxyz!"; //>= 256 bits

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extraer roles del token
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("roles", List.class);  // Recuperar los roles del token
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Generar JWT Token
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8)) // Token válido por 8 horas
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Firmar el token con la clave segura
				.compact();
	}

	// Validar el JWT
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean validateToken(String token, UserDetails usuarioDetails) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(usuarioDetails.getUsername()) && !isTokenExpired(token));
	}
}
