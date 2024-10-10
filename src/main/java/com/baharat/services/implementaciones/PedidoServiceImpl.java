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
		Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
		pedido.setEstado(nuevoEstado);
		return pedidoRepository.save(pedido);
	}

	@Override
	public Optional<Pedido> obtenerPedidoPorId(Integer idPedido) {
		return Optional.ofNullable(pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
	}

	private void verificarStock(DetallePedido detalle,
	                            Pedido pedido) {
		Producto productoBd = productoRepository.findByNombreProducto(detalle.getNombreProducto()).orElseThrow(
				() -> new RuntimeException("Producto no encontrado")
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
		pedido.setEstado("PROCESADO");
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
		PDPage page = new PDPage(PDRectangle.A5); // Crear una página tamaño A5
		document.addPage(page);

		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Escribir el contenido del pedido en el PDF
		contentStream.beginText();

		//Header
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
		contentStream.newLineAtOffset(74, 550); // Posicionar el texto en el documento
		contentStream.showText("BAHARAT ALMACEN NATURAL");

		// Información del pedido
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
		contentStream.newLineAtOffset(-24, -40);
		contentStream.showText("Factura - Pedido #" + pedido.getIdPedido());
		contentStream.newLineAtOffset(0, -20);
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.showText("Cliente: " + pedido.getCliente());
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("Fecha: " + pedido.getFechaPedido().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		// Listado de productos del pedido
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("-------------------------------------------------------------------------");
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("CANT   DESCRIPCION                    PRECIO");
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("-------------------------------------------------------------------------");
		contentStream.endText();

		float yPosition = 415;
		Integer cantidadTotal = 0;
		for (DetallePedido detalle : pedido.getListadoDetallePedidos()) {
			establecerNombreProducto(detalle);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 12);
			contentStream.newLineAtOffset(60, yPosition);
			contentStream.showText(detalle.getCantidad() + "        " + detalle.getNombreProducto() + "                  " + detalle.getSubTotal());
			contentStream.endText();
			cantidadTotal += detalle.getCantidad();
			yPosition -= 20; // Reducir el valor para bajar la línea
		}

		// Total del pedido
		contentStream.beginText();
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.newLineAtOffset(50, yPosition);
		contentStream.showText("-------------------------------------------------------------------------");
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("   " + cantidadTotal + "               TOTAL     PESOS         " + pedido.getTotal());
		contentStream.newLineAtOffset(0, -10);
		contentStream.showText("-------------------------------------------------------------------------");

		// Finalizar el contenido
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

}
