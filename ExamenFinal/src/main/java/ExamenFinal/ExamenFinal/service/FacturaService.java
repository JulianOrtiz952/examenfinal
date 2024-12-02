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


    private TiendaRepository tiendaRepository;

    private ClienteRepository clienteRepository;

    private TipoDocumentoRepository tipoDocumentoRepository;

    private VendedorRepository vendedorRepository;

    private CajeroRepository cajeroRepository;

    private ProductoRepository productoRepository;

    private TipoPagoRepository tipoPagoRepository;

    private CompraRepository compraRepository;

    private DetallesCompraRepository detallesCompraRepository;

    private PagoRepository pagoRepository;

    @Transactional
    public void processInvoice(String tiendaId, FacturaDto facturaDto) {
        Tienda tienda = tiendaRepository.findByUuid(tiendaId)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));

        Cliente cliente = processCliente(facturaDto.getCliente());
        Vendedor vendedor = vendedorRepository.findByDocumento(facturaDto.getVendedor().getDocumento())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
        Cajero cajero = cajeroRepository.findByToken(facturaDto.getCajero().getToken())
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setTienda(tienda);
        compra.setVendedor(vendedor);
        compra.setCajero(cajero);
        compra.setFecha(LocalDateTime.now());
        compra.setImpuestos(facturaDto.getImpuesto());

        List<DetallesCompra> detallesCompraList = processProductos(facturaDto.getProductos(), compra);
        compra.setDetallesCompra(detallesCompraList);

        double total = detallesCompraList.stream()
                .mapToDouble(dc -> dc.getPrecio() * dc.getCantidad() * (1 - dc.getDescuento() / 100))
                .sum();
        compra.setTotal(total + facturaDto.getImpuesto());

        compraRepository.save(compra);

        processPagos(facturaDto.getMedios_pago(), compra);
    }

    private Cliente processCliente(ClienteDto clienteDto) {
        Cliente cliente = clienteRepository.findByDocumento(clienteDto.getDocumento())
                .orElse(new Cliente());
        cliente.setNombre(clienteDto.getNombre());
        cliente.setDocumento(clienteDto.getDocumento());
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombre(String.valueOf(clienteDto.getTipo_documento()))
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
        cliente.setTipoDocumento(tipoDocumento);
        return clienteRepository.save(cliente);
    }

    private List<DetallesCompra> processProductos(List<ProductoDto> productosDto, Compra compra) {
        List<DetallesCompra> detallesCompraList = new ArrayList<>();
        for (ProductoDto productoDto : productosDto) {
            Producto producto = productoRepository.findByReferencia(productoDto.getReferencia())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoDto.getReferencia()));
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
        for (MedioPagoDto medioPagoDto : mediosPagoDto) {
            Pago pago = new Pago();
            pago.setCompra(compra);
            TipoPago tipoPago = tipoPagoRepository.findByNombre(medioPagoDto.getTipo_pago())
                    .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado: " + medioPagoDto.getTipo_pago()));
            pago.setTipoPago(tipoPago);
            pago.setTarjetaTipo(medioPagoDto.getTipo_tarjeta());
            pago.setCuotas(medioPagoDto.getCuotas());
            pago.setValor(medioPagoDto.getValor());
            pagoRepository.save(pago);
        }
    }

}
