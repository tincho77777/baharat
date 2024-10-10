package com.baharat.repositories;

import com.baharat.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByEstado(String nombreProducto);
}
