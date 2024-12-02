package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vendedor")
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String documento;
    private String email;
}
