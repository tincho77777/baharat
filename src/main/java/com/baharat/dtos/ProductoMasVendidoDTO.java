package com.baharat.dtos;

import com.baharat.models.entities.Producto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductoMasVendidoDTO {

	private Producto producto;
	@JsonProperty("cantidad_vendida")
	private Long cantidadVendida;

	public void ProductoVendidoDTO(Producto producto, Long cantidadVendida) {
		this.producto = producto;
		this.cantidadVendida = cantidadVendida;
	}
}
