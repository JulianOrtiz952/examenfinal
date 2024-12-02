package ExamenFinal.ExamenFinal.dto;

import lombok.Data;

@Data
public class MedioPagoDto {
    private String tipo_pago;
    private String tipo_tarjeta;
    private Integer cuotas;
    private double valor;
}
