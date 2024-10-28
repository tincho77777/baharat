package com.baharat.repositories;

import com.baharat.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByEstado(String nombreProducto);
}
