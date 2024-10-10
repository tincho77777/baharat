package com.baharat.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	@JsonProperty("id_producto")
	private Integer idProducto;

	@Size(min = 1, max = 255)
	@NotNull
	@Column(name = "nombre_producto")
	@JsonProperty("nombre_producto")
	private String nombreProducto;

	@NotNull
	@Size(max = 100)
	@Column(name = "categoria")
	private String categoria;

	@Min(0)
	@Column(name = "stock")
	private Integer stock;

	@Column(name = "fecha_ingreso")
	@JsonProperty("fecha_ingreso")
	private LocalDate fechaIngreso;

	@NotNull
	@Min(0)
	@Column(name = "precio")
	private BigDecimal precio;

	@NotNull
	@Size(max = 255)
	@Column(name = "proveedor")
	private String proveedor;

	@Column(name = "es_gluten_free")
	@JsonProperty("es_gluten_free")
	private Boolean isGlutenFree;

	@Column(name = "es_vegano")
	@JsonProperty("es_vegano")
	private Boolean isVegano;

	@JsonIgnore
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DetallePedido> listadoDetalles;

	@Override
	public String toString() {
		return "Producto{" +
				"idProducto=" + idProducto +
				", nombreProducto='" + nombreProducto + '\'' +
				", categoria='" + categoria + '\'' +
				", stock=" + stock +
				", fechaIngreso=" + fechaIngreso +
				", precio=" + precio +
				", proveedor='" + proveedor + '\'' +
				", isGlutenFree=" + isGlutenFree +
				", isVegano=" + isVegano +
				'}';
	}
}
