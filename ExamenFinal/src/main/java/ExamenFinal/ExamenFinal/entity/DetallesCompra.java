package ExamenFinal.ExamenFinal.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalles_compra")
public class DetallesCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;
    private Double precio;
    private Double descuento;
}