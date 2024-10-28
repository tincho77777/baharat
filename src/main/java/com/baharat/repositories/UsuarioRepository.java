package com.baharat.repositories;

import com.baharat.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Usuario findByUsername(String username);

	Optional<Usuario> findByEmail(String email);

	boolean existsByUsername(String username);
}
