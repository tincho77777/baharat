package com.baharat.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido")
	@JsonProperty("id_pedido")
	private Integer idPedido;

	@Column(name = "fecha_pedido")
	@JsonProperty("fecha_pedido")
	private LocalDate fechaPedido;

	@Size(max = 255)
	@Column(name = "cliente")
	private String cliente;

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonProperty("detalle_pedidos")
	private List<DetallePedido> listadoDetallePedidos;

	@Min(0)
	@Column(name = "total")
	private BigDecimal total;

	@Size(max = 50)
	@Column(name = "estado")
	private String estado;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "producto")
	private Producto producto;

	@Override
	public String toString() {
		return "Pedido{" +
				"idPedido=" + idPedido +
				", fechaPedido=" + fechaPedido +
				", cliente='" + cliente + '\'' +
				", total=" + total +
				", estado='" + estado + '\'' +
				'}';
	}

	public void setearEstadoPedido() {
		if (producto != null) {
			this.estado = "PROCESADO";
		} else {
			this.estado = "ERROR";
		}
	}
}
