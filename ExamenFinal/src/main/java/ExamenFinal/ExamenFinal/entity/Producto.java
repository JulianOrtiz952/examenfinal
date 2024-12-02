package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "tipo_producto_id")
    private TipoProducto tipoProducto;
}
