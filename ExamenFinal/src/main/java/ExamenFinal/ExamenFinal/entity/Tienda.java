package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tienda")
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String direccion;
    private String uuid;

    @OneToMany(mappedBy = "tienda")
    private List<Cajero> cajeros;

    @OneToMany(mappedBy = "tienda")
    private List<Compra> compras;
}