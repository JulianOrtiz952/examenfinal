package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cajero")
public class Cajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String documento;
    private String email;
    private String token;

    @ManyToOne
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;
}
