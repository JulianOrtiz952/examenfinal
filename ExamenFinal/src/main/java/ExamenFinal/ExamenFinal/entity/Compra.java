package ExamenFinal.ExamenFinal.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "cajero_id")
    private Cajero cajero;

    private Double total;
    private Double impuestos;
    private LocalDateTime fecha;
    private String observaciones;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetallesCompra> detallesCompra;

    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL)
    private Pago pago;
}
