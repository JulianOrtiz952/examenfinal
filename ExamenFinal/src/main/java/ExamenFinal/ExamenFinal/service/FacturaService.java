package ExamenFinal.ExamenFinal.service;

import ExamenFinal.ExamenFinal.dto.ClienteDto;
import ExamenFinal.ExamenFinal.dto.FacturaDto;
import ExamenFinal.ExamenFinal.dto.MedioPagoDto;
import ExamenFinal.ExamenFinal.dto.ProductoDto;
import ExamenFinal.ExamenFinal.entity.*;
import ExamenFinal.ExamenFinal.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {


    private final TiendaRepository tiendaRepository;

    private final ClienteRepository clienteRepository;

    private final TipoDocumentoRepository tipoDocumentoRepository;

    private final VendedorRepository vendedorRepository;

    private final CajeroRepository cajeroRepository;

    private final ProductoRepository productoRepository;

    private final TipoPagoRepository tipoPagoRepository;

    private final CompraRepository compraRepository;

    private final DetallesCompraRepository detallesCompraRepository;

    private final PagoRepository pagoRepository;


    @Transactional
    public void processInvoice(String tiendaId, FacturaDto facturaDto) {
        Tienda tienda = tiendaRepository.findByUuid(tiendaId)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con UUID: " + tiendaId));

        Cliente cliente = processCliente(facturaDto.getCliente());
        Vendedor vendedor = vendedorRepository.findByDocumento(facturaDto.getVendedor().getDocumento())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con documento: " + facturaDto.getVendedor().getDocumento()));
        Cajero cajero = cajeroRepository.findByToken(facturaDto.getCajero().getToken())
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado con token: " + facturaDto.getCajero().getToken()));

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setTienda(tienda);
        compra.setVendedor(vendedor);
        compra.setCajero(cajero);
        compra.setFecha(LocalDateTime.now());
        compra.setImpuestos(facturaDto.getImpuesto());

        List<DetallesCompra> detallesCompraList = processProductos(facturaDto.getProductos(), compra);
        compra.setDetallesCompra(detallesCompraList);

        double subtotal = detallesCompraList.stream()
                .mapToDouble(dc -> dc.getPrecio() * dc.getCantidad() * (1 - dc.getDescuento() / 100.0))
                .sum();
        double total = subtotal + facturaDto.getImpuesto();
        compra.setTotal(total);

        compra = compraRepository.save(compra);

        processPagos(facturaDto.getMedios_pago(), compra);
    }

    private Cliente processCliente(ClienteDto clienteDto) {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombre(clienteDto.getTipo_documento())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado: " + clienteDto.getTipo_documento()));

        List<Cliente> clientes = clienteRepository.findByDocumentoAndTipoDocumento(clienteDto.getDocumento(), tipoDocumento);

        Cliente cliente;
        if (clientes.isEmpty()) {
            // Create a new client if not found
            cliente = new Cliente();
            cliente.setDocumento(clienteDto.getDocumento());
            cliente.setTipoDocumento(tipoDocumento);
        } else if (clientes.size() == 1) {
            // Use the existing client if only one is found
            cliente = clientes.get(0);
        } else {
            // If multiple clients are found, use the first one and log a warning
            cliente = clientes.get(0);
            System.out.println("WARNING: Multiple clients found with documento " + clienteDto.getDocumento() + " and tipo_documento " + clienteDto.getTipo_documento() + ". Using the first one.");
        }

        // Update the client's name
        cliente.setNombre(clienteDto.getNombre());
        return clienteRepository.save(cliente);
    }

    private List<DetallesCompra> processProductos(List<ProductoDto> productosDto, Compra compra) {
        List<DetallesCompra> detallesCompraList = new ArrayList<>();
        for (ProductoDto productoDto : productosDto) {
            Producto producto = productoRepository.findByReferencia(productoDto.getReferencia())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con referencia: " + productoDto.getReferencia()));
            DetallesCompra detallesCompra = new DetallesCompra();
            detallesCompra.setCompra(compra);
            detallesCompra.setProducto(producto);
            detallesCompra.setCantidad(productoDto.getCantidad());
            detallesCompra.setPrecio(producto.getPrecio());
            detallesCompra.setDescuento(productoDto.getDescuento());
            detallesCompraList.add(detallesCompra);
        }
        return detallesCompraRepository.saveAll(detallesCompraList);
    }

    private void processPagos(List<MedioPagoDto> mediosPagoDto, Compra compra) {
        double totalPagado = 0;
        StringBuilder detallesPago = new StringBuilder();

        for (MedioPagoDto medioPagoDto : mediosPagoDto) {
            TipoPago tipoPago = tipoPagoRepository.findByNombre(medioPagoDto.getTipo_pago())
                    .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado: " + medioPagoDto.getTipo_pago()));

            totalPagado += medioPagoDto.getValor();
            detallesPago.append(tipoPago.getNombre())
                    .append(": ")
                    .append(String.format("%.2f", medioPagoDto.getValor()))
                    .append("; ");
        }

        Pago pago = new Pago();
        pago.setCompra(compra);
        pago.setValor(totalPagado);
        pago.setObservaciones(detallesPago.toString());

        pagoRepository.save(pago);

        // Verificar si el total pagado coincide con el total de la compra
        if (Math.abs(totalPagado - compra.getTotal()) > 0.01) {
            throw new RuntimeException("El total pagado (" + String.format("%.2f", totalPagado) +
                    ") no coincide con el total de la compra (" +
                    String.format("%.2f", compra.getTotal()) + ")");
        }
    }
}