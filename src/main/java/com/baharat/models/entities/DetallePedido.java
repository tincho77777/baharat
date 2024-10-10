package com.baharat.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle")
	@JsonProperty("id_detalle")
	private Integer idDetalle;

	@Min(0)
	@Column(name = "cantidad")
	private Integer cantidad;

	@Min(0)
	@Column(name = "subtotal")
	@JsonProperty("subtotal")
	private BigDecimal subTotal;

	@Transient // Agregar esta anotación si no se almacena en la base de datos
	@JsonProperty("nombre_producto")
	private String nombreProducto; // Campo para almacenar el nombre del producto

	@ManyToOne
	// Relación con Producto, como clave foranea, lo que obtiene es la clave primaria de esta, o sea el id impactara en la base
	@JoinColumn(name = "id_producto", nullable = false)
	@JsonIgnore
	private Producto producto;

	@ManyToOne
	// Relación con Pedido, como clave foranea, lo que obtiene es la clave primaria de esta, o sea el id impactara en la base
	@JoinColumn(name = "id_pedido", nullable = false)
	@JsonIgnore
	private Pedido pedido;

	// Cambiamos a los getters para manejar la salida correcta de los IDs
	@JsonProperty("id_producto")
	public Integer getIdProducto() {
		return producto != null ? producto.getIdProducto() : null;
	}

	@JsonProperty("id_pedido")
	public Integer getIdPedido() {
		return pedido != null ? pedido.getIdPedido() : null;
	}

	public void calcularSubtotal() {
		if (producto != null && cantidad != null) {
			this.subTotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
		}
	}
}
