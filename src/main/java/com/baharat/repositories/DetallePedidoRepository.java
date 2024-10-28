package com.baharat.repositories;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
	List<DetallePedido> findByPedido(Pedido pedido);
}
