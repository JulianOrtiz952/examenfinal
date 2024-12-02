package ExamenFinal.ExamenFinal.repository;

import ExamenFinal.ExamenFinal.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
}
