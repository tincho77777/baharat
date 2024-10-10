package com.baharat.repositories;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
	List<DetallePedido> findByPedido(Pedido pedido);
}
