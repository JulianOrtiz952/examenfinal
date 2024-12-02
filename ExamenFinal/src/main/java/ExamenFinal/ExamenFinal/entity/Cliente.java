package ExamenFinal.ExamenFinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String documento;

    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;
}
