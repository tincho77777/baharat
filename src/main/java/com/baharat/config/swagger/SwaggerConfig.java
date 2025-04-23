package com.baharat.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	// TAGS
	public static final String PRODUCTOS_TAG = "Gestión de Productos";
	public static final String DETALLE_PEDIDO_TAG = "Gestión de Detalle de Pedidos";
	public static final String PEDIDOS_TAG = "Gestión de Pedidos";
	public static final String REPORTES_TAG = "Gestión de Reportes";
	public static final String USUARIOS_TAG = "Gestión de Usuarios";

	// TAG DESCRIPTIONS
	public static final String PRODUCTO_TAG_DESCRIPTION = "Operaciones relacionadas a productos disponibles en el sistema.";
	public static final String DETALLE_PEDIDO_TAG_DESCRIPTION = "Operaciones relacionadas a detalles de pedidos disponibles en el sistema.";
	public static final String PEDIDO_TAG_DESCRIPTION = "Operaciones relacionadas a pedidos disponibles en el sistema.";
	public static final String USUARIO_TAG_DESCRIPTION = "Operaciones relacionadas a usuarios disponibles en el sistema.";

	// OPERATIONS
	public static final String LISTAR_PRODUCTOS_SUMMARY = "Listar todos los productos";
	public static final String OBTENER_PRODUCTO_POR_ID_SUMMARY = "Obtener producto por Id";
	public static final String CONSULTAR_STOCK_SUMMARY = "Consultar stock por nombre";
	public static final String OBTENER_PRODUCTO_POR_NOMBRE_SUMMARY = "Obtener producto por nombre";
	public static final String AGREGAR_PRODUCTO_SUMMARY = "Agregar nuevo producto";
	public static final String ELIMINAR_PRODUCTO_SUMMARY = "Eliminar producto por Id";
	public static final String EDITAR_PRODUCTO_SUMMARY = "Editar producto existente";

	public static final String OBTENER_DETALLE_POR_ID_SUMMARY = "Obtener detalle por Id";
	public static final String OBTENER_DETALLE_POR_ID_PEDIDO_SUMMARY = "Obtener detalle por Id de pedido";

	public static final String AGREGAR_PEDIDO_SUMMARY = "Agregar nuevo pedido";
	public static final String OBTENER_PEDIDO_POR_ID_SUMMARY = "Obtener pedido por id";
	public static final String OBTENER_PEDIDOS_POR_ESTADO_SUMMARY = "Obtener pedidos por estado";
	public static final String OBTENER_FACTURA_SUMMARY = "Obtener factrua de pedido";

	public static final String LISTAR_PRODUCTOS_PREFERIDOS_SUMMARY = "Listar productos preferidos";
	public static final String LISTAR_PRODUCTOS_CON_BAJO_STOCK_SUMMARY = "Listar productos con poco stock";
	public static final String LISTAR_VENTAS_TOTALES_SUMMARY = "Listar ventas totales";

	public static final String AGREGAR_USUARIO_SUMMARY = "Agregar nuevo usuario";
	public static final String EDITAR_ROL_USUARIO_SUMMARY = "Editar el rol de un usuario";
	public static final String LISTAR_USUARIOS_SUMMARY = "Listar usuarios";
	public static final String ELIMINAR_USUARIO_SUMMARY = "Eliminar usuario";

	// RESPONSES
	public static final String RESPUESTA_200 = "Operación exitosa";
	public static final String RESPUESTA_201 = "Recurso creado exitosamente";
	public static final String RESPUESTA_204 = "No se encontraron resultados";
	public static final String RESPUESTA_400 = "Solicitud incorrecta";
	public static final String RESPUESTA_404 = "Recurso no encontrado";
	public static final String RESPUESTA_409 = "Conflicto: recurso ya existente";
	public static final String RESPUESTA_500 = "Error interno del servidor";

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Baharat")
						.version("v1")
						.description("Endpoints disponibles")
						.contact(new Contact().name("Martin Alvarez").email("martinalvarez12121@gmail.com").url("baharat.com"))
				);
	}

}