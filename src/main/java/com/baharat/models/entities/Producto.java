package com.baharat.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	@NotBlank(message = "El nombre del producto no puede estar vacío")
	@Column(name = "nombre_producto")
	@JsonProperty("nombre_producto")
	private String nombreProducto;

	@NotBlank(message = "La categoria del producto no puede estar vacío")
	@Size(max = 100)
	@Column(name = "categoria")
	private String categoria;

	@Min(0)
	@NotNull(message = "El stock del producto no puede ser nulo")
	@Column(name = "stock")
	private Integer stock;
	@NotNull(message = "La fecha de ingreso del producto no puede ser nula")
	@Column(name = "fecha_ingreso")
	@JsonProperty("fecha_ingreso")
	private LocalDate fechaIngreso;

	@NotNull(message = "El precio del producto no puede ser nulo")
	@Min(0)
	@Column(name = "precio")
	private BigDecimal precio;

	@NotNull(message = "El proveedor del producto no puede ser nulo")
	@Size(max = 255)
	@Column(name = "proveedor")
	private String proveedor;

	@Column(name = "es_gluten_free")
	@NotNull(message = "El campo isGlutenFree no puede ser nulo")
	@JsonProperty("es_gluten_free")
	private Boolean isGlutenFree;

	@Column(name = "es_vegano")
	@JsonProperty("es_vegano")
	@NotNull(message = "El campo isVegano no puede ser nulo")
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
