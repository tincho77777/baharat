package com.baharat.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@NotBlank(message = "El nombre de usuario no puede ser nulo")
	@Column(name = "username", nullable = false)
	private String username;

	@NotBlank(message = "La contraseña no puede ser nula")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "fecha_alta", nullable = false)
	private LocalDate fechaAlta;

	@NotBlank(message = "El email no puede ser nulo")
	@Email(message = "El email debe tener un formato válido")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "rol", nullable = false)
	private String rol;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.rol));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Podrías usar un campo para manejar esto si es necesario
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Podrías usar un campo para manejar esto si es necesario
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Podrías usar un campo para manejar esto si es necesario
	}

	@Override
	public boolean isEnabled() {
		return true; // Podrías usar un campo booleano "enabled" para gestionar si el usuario está habilitado o no
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Usuario usuario)) return false;
		return idUsuario != null && idUsuario.equals(usuario.idUsuario);
	}

	@Override
	public int hashCode() {
		return 31; // Puedes usar un valor constante, ya que el ID es el único que usas.
	}
}
