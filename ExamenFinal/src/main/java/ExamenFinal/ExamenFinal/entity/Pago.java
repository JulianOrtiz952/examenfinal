package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "tipo_pago_id")
    private TipoPago tipoPago;

    private String tarjetaTipo;
    private Integer cuotas;
    private Double valor;
}
