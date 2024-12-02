package ExamenFinal.ExamenFinal.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class FacturaDto {

    private double impuesto;
    private ClienteDto cliente;
    private List<ProductoDto> productos;
    private List<MedioPagoDto> medios_pago;
    private VendedorDto vendedor;
    private CajeroDto cajero;
}
