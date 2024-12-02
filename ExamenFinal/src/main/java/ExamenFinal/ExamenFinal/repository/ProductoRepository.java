package ExamenFinal.ExamenFinal.repository;

import ExamenFinal.ExamenFinal.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByReferencia(String referencia);
}
