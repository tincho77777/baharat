package com.baharat.services.implementaciones;

import com.baharat.models.entities.DetallePedido;
import com.baharat.models.entities.Pedido;
import com.baharat.models.entities.Producto;
import com.baharat.repositories.PedidoRepository;
import com.baharat.repositories.ProductoRepository;
import com.baharat.services.PedidoService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.baharat.parametros.Mensajes.ERROR_PEDIDO_NO_ENCONTRADO;
import static com.baharat.parametros.Mensajes.ERROR_PRODUCTO_NO_ENCONTRADO;
import static com.baharat.parametros.Parametros.PROCESADO;

@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	protected ProductoRepository productoRepository;
	@Autowired
	protected PedidoRepository pedidoRepository;

	@Transactional
	@Override
	public Pedido procesarPedido(Pedido pedido) {
		BigDecimal total = BigDecimal.ZERO;

		// Verificar que todos los productos del pedido tienen suficiente stock
		for (DetallePedido detalle : pedido.getListadoDetallePedidos()) {
			verificarStock(detalle, pedido);
			total = total.add(detalle.getSubTotal());
		}

		// Si está bien, procede a descontar el stock
		for (DetallePedido detalle : pedido.getListadoDetallePedidos()) {
			descontarStock(detalle);
		}

		// Guardamos el pedido
		guardarPedido(pedido, total);

		return pedidoRepository.save(pedido);
	}

	@Override
	public List<Pedido> obtenerPedidosPorEstado(String estado) {
		return pedidoRepository.findByEstado(estado);
	}

	@Override
	public Pedido actualizarEstadoPedido(Integer idPedido, String nuevoEstado) {
		Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException(ERROR_PEDIDO_NO_ENCONTRADO));
		pedido.setEstado(nuevoEstado);
		return pedidoRepository.save(pedido);
	}

	@Override
	public Optional<Pedido> obtenerPedidoPorId(Integer idPedido) {
		return Optional.ofNullable(pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException(ERROR_PEDIDO_NO_ENCONTRADO)));
	}

	private void verificarStock(DetallePedido detalle,
	                            Pedido pedido) {
		Producto productoBd = productoRepository.findByNombreProducto(detalle.getNombreProducto()).orElseThrow(
				() -> new RuntimeException(ERROR_PRODUCTO_NO_ENCONTRADO)
		);

		if (productoBd.getStock() < detalle.getCantidad()) {
			throw new RuntimeException("No hay suficiente stock para el producto: " + productoBd.getNombreProducto());
		}

		setearDatosDetalle(detalle, pedido, productoBd);
	}

	private void descontarStock(DetallePedido detalle) {
		Producto productoBd = productoRepository.findById(detalle.getProducto().getIdProducto()).orElseThrow();
		productoBd.setStock(productoBd.getStock() - detalle.getCantidad());
		productoRepository.save(productoBd);
	}

	private void guardarPedido(Pedido pedido,
	                           BigDecimal total) {
		pedido.setFechaPedido(LocalDate.now());
		pedido.setEstado(PROCESADO);
		pedido.setTotal(total);
	}

	private void setearDatosDetalle(DetallePedido detalle,
	                                Pedido pedido,
	                                Producto productoBd) {
		try {
			detalle.setPedido(pedido);
			detalle.setProducto(productoBd);
			detalle.calcularSubtotal();
		} catch (Exception e) {
			//ver error por lanzar
		}
	}

	public byte[] generarFacturaPDF(Optional<Pedido> pedidoOptional) throws IOException {

		if (pedidoOptional.isEmpty()) {
			throw new IllegalArgumentException("Pedido no encontrado");
		}
		Pedido pedido = pedidoOptional.get();

		// Crear un nuevo documento PDF
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A6); // Página tamaño A6 para dar aspecto de ticket
		document.addPage(page);

		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Usar una fuente de ancho fijo para evitar problemas de alineación
		contentStream.setFont(PDType1Font.COURIER_BOLD, 10);
		contentStream.beginText();

		// Encabezado centrado
		contentStream.newLineAtOffset(75, 400);
		contentStream.showText("BAHARAT ALMACEN NATURAL");

		// Información del negocio
		contentStream.setFont(PDType1Font.COURIER, 8);
		contentStream.newLineAtOffset(-30, -20);
		contentStream.showText("Paso de los Andes 1628, Godoy Cruz");
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("Tel: (261) 123-4567");

		// Información del pedido
		contentStream.newLineAtOffset(0, -20);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 8);
		contentStream.showText("Factura - Pedido #" + pedido.getIdPedido());
		contentStream.newLineAtOffset(0, -10);
		contentStream.setFont(PDType1Font.COURIER, 8);
		contentStream.showText("Cliente: " + pedido.getCliente());
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("Fecha: " + pedido.getFechaPedido().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		// Separador de encabezado
		String lineaSeparador = "****************************************";
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText(lineaSeparador);

		// Tabla de productos
		contentStream.newLineAtOffset(0, -12);
		contentStream.showText("CANT  DESCRIPCION          PRECIO");
		contentStream.newLineAtOffset(0, -12);
		contentStream.showText(lineaSeparador);
		contentStream.endText();

		float yPosition = 270;
		Integer cantidadTotal = 0;

		// Listado de productos
		for (DetallePedido detalle : pedido.getListadoDetallePedidos()) {
			establecerNombreProducto(detalle);

			// Configurar las columnas
			String cantidad = String.format("%-4s", detalle.getCantidad().toString());
			String descripcion = cortarTexto(detalle.getNombreProducto());
			String precio = String.format("%7s", String.format("%.2f", detalle.getSubTotal()));

			contentStream.beginText();
			contentStream.setFont(PDType1Font.COURIER, 8);

			// Cantidad
			contentStream.newLineAtOffset(50, yPosition);
			contentStream.showText(cantidad);

			// Descripción
			contentStream.newLineAtOffset(25, 0);
			contentStream.showText(descripcion);

			// Precio
			contentStream.newLineAtOffset(95, 0);
			contentStream.showText(precio);

			contentStream.endText();

			cantidadTotal += detalle.getCantidad();
			yPosition -= 15;
		}

		// Totales y resumen (mantener después de la lista de productos)
		contentStream.beginText();
		contentStream.setFont(PDType1Font.COURIER, 8);
		contentStream.newLineAtOffset(45, yPosition - 10);
		contentStream.showText(lineaSeparador);

		contentStream.newLineAtOffset(0, -12);
		contentStream.showText(" TOTAL CANTIDAD: " + cantidadTotal);
		contentStream.newLineAtOffset(0, -12);
		contentStream.showText(" TOTAL PESOS: " + String.format("%.2f", pedido.getTotal()));

		contentStream.newLineAtOffset(0, -12);
		contentStream.showText(lineaSeparador);
		contentStream.endText();

		// Pie de página en la parte inferior de la página
		contentStream.beginText();
		contentStream.setFont(PDType1Font.COURIER, 8);

		float footerYPosition = 30; // Parte inferior de la página
		contentStream.newLineAtOffset(45, footerYPosition);
		contentStream.showText(lineaSeparador);
		contentStream.newLineAtOffset(0, 12);
		contentStream.showText("¡Gracias por su compra!");
		contentStream.newLineAtOffset(0, 12);
		contentStream.showText("Horario: Lunes a Sábado 9:00 - 21:00");
		contentStream.newLineAtOffset(0, 12);
		contentStream.showText("Consultas: info@baharat.com");
		contentStream.newLineAtOffset(0, 12);
		contentStream.showText("No se aceptan devoluciones sin factura.");
		contentStream.newLineAtOffset(0, 12);
		contentStream.showText(lineaSeparador);

		contentStream.endText();
		contentStream.close();

		// Escribir el PDF en un ByteArrayOutputStream para devolverlo como byte[]
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream);
		document.close();

		return outputStream.toByteArray();
	}

	public void establecerNombreProducto(DetallePedido detalle) {
		if (detalle != null && detalle.getProducto() != null) {
			detalle.setNombreProducto(detalle.getProducto().getNombreProducto());
		}
	}

	// Función para cortar el texto si excede una longitud fija
	private String cortarTexto(String texto) {
		if (texto.length() > 18) {
			return texto.substring(0, 18 - 3) + "...";
		}
		return texto;
	}

}
